package org.flyak.api.service;

import org.flyak.api.data.entity.PasswordReset;
import org.flyak.api.data.entity.User;
import org.flyak.api.data.misc.Mail;
import org.flyak.api.data.repository.PasswordResetRepository;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.dto.ForgotPasswordRequest;
import org.flyak.api.dto.LoginRequest;
import org.flyak.api.dto.RegisterRequest;
import org.flyak.api.utils.PasswordGenerator;
import org.flyak.api.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final AuthenticationManager authenticationManager;
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
                       UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.passwordResetRepository = passwordResetRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
        this.emailService = emailService;
        this.userService = userService;
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
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setVerified(false);
        user.setVerification_token(token);
        userRepository.save(user);

        Mail mail = new Mail(user.getEmail(), "Welcome to FlyAK, Verify Registration", "email-registration");
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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }
}
