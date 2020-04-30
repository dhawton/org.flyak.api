package org.flyak.api.service;

import org.flyak.api.data.entity.User;
import org.flyak.api.data.misc.Mail;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.dto.LoginRequest;
import org.flyak.api.dto.RegisterRequest;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    @Value("${app.ui.baseurl}")
    private String UIBaseURL;
    @Value("${app.ui.registration_verification}")
    private String UIRegVerificationURL;
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
        this.emailService = emailService;
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
        props.put("verification_url", String.format("%s%s%s", UIBaseURL, UIRegVerificationURL, token));
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
