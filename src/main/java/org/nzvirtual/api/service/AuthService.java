package org.nzvirtual.api.service;

import org.nzvirtual.api.data.entity.PasswordReset;
import org.nzvirtual.api.data.entity.RefreshToken;
import org.nzvirtual.api.data.entity.User;
import org.nzvirtual.api.data.misc.Mail;
import org.nzvirtual.api.data.repository.PasswordResetRepository;
import org.nzvirtual.api.data.repository.RefreshTokenRepository;
import org.nzvirtual.api.data.repository.UserRepository;
import org.nzvirtual.api.dto.LoginRequest;
import org.nzvirtual.api.dto.RegisterRequest;
import org.nzvirtual.api.utils.PasswordGenerator;
import org.nzvirtual.api.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGenerator tokenGenerator;
    @Value("${app.ui.baseurl}")
    private String UIBaseURL;
    @Value("${app.ui.registration_verification}")
    private String UIRegVerificationPath;
    @Value("${app.ui.forgotpassword_verification}")
    private String UIForgotPasswordPath;
    private final EmailService emailService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(PasswordEncoder passwordEncoder,
                       PasswordResetRepository passwordResetRepository,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       EmailService emailService,
                       UserService userService,
                       RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
        this.emailService = emailService;
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public Boolean verifyAccount(String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        user.setVerified(true);
        user.setVerification_token(null);
        userRepository.save(user);

        return true;
    }

    @Transactional
    public Boolean checkAndReset(String token) {
        Optional<PasswordReset> passwordResetOptional = passwordResetRepository.findByToken(token);
        if (passwordResetOptional.isEmpty()) {
            return false;
        }

        PasswordReset passwordReset = passwordResetOptional.get();
        User user = passwordReset.getUser();
        String newPassword = PasswordGenerator.generateRandomPassword(12);
        userService.changePassword(user, newPassword);
        log.info(String.format("User %s (%s) password reset, token verified", user.getName(), user.getEmail()));
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("newpassword", newPassword);
        emailService.generateEmailRequest(user.getEmail(), "Password has been reset", "email-passwordreset", props);

        passwordResetRepository.delete(passwordResetOptional.get());
        return true;
    }

    @Transactional
    public String createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Transactional
    public User lookupToken(String token) throws Exception {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);
        if (optionalRefreshToken.isEmpty())
            throw new Exception("Not Found");

        return optionalRefreshToken.get().getUser();
    }

    @Transactional
    @Async
    public void deleteToken(String token) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);
        if (optionalRefreshToken.isEmpty()) {
            log.error("Attempted to invalidate token " + token + " but it doesn't exist.");
            return;
        }

        refreshTokenRepository.delete(optionalRefreshToken.get());
    }

    @Transactional
    @Async
    public void blacklistRefreshTokenForUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void forgotPassword(User user) {
        String token = tokenGenerator.nextString();

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setToken(token);
        passwordReset.setUser(user);
        passwordReset.setCreated_at(new Date());
        passwordResetRepository.save(passwordReset);

        Map<String, Object> props = new HashMap<>();
        props.put("verification_url", String.format("%s%s%s", UIBaseURL, UIForgotPasswordPath, token));

        log.info(String.format("Password reset requested for %s (%s)", user.getName(), user.getEmail()));
        emailService.generateEmailRequest(
            String.format("%s <%s>", user.getName(), user.getEmail()),
            "Forget your password?",
            "email-forgotpassverification",
            props
        );
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        String token = tokenGenerator.nextString();

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setVerified(false);
        user.setVerification_token(token);
        userRepository.save(user);

        Mail mail = new Mail(user.getEmail(), "Welcome to NZVirtual, Verify Registration", "email-registration");
        Map<String,Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("verification_url", String.format("%s%s%s", UIBaseURL, UIRegVerificationPath, token));
        mail.setProps(props);
        try {
            emailService.sendEmail(mail);
        } catch(MessagingException e) {
            log.error(String.format("Caught MessagingException during registration of %s, %s", user.getEmail(), e.getLocalizedMessage()));
        } catch(IOException e) {
            log.error(String.format("Caught IOException during registration of %s, %s", user.getEmail(), e.getLocalizedMessage()));
        }
    }

    public void login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    }
}
