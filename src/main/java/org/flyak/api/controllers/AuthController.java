package org.flyak.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.flyak.api.data.misc.Mail;
import org.flyak.api.dto.LoginRequest;
import org.flyak.api.dto.RegisterRequest;
import org.flyak.api.dto.TokenResponse;
import org.flyak.api.exception.ErrorResponse;
import org.flyak.api.exception.GeneralException;
import org.flyak.api.exception.ValidationException;
import org.flyak.api.security.JwtUtils;
import org.flyak.api.security.UserDetailsImpl;
import org.flyak.api.service.AuthService;
import org.flyak.api.service.EmailService;
import org.flyak.api.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Value("${app.registration.enabled}")
    private Boolean registrationEnabled;
    private Logger log = LoggerFactory.getLogger(AuthController.class);
    @Value("${app.ui.baseurl}")
    private String UIBaseURL;
    @Value("${app.ui.registration_verification}")
    private String UIRegVerificationURL;
    private TokenGenerator tokenGenerator;
    private EmailService emailService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailService emailService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public void test() {
        String token = tokenGenerator.nextString();

        Mail mail = new Mail("daniel@hawton.org", "Welcome to FlyAK, Verify Registration", "email-registration");
        Map<String,Object> props = new HashMap<>();
        props.put("name", "Daniel Hawton");
        props.put("verification_url", String.format("%s%s%s", UIBaseURL, UIRegVerificationURL, token));
        mail.setProps(props);
        try {
            emailService.sendEmail(mail);
        } catch(MessagingException e) {
            log.error(String.format("Caught MessagingException during test %s", e.getCause()));
        } catch(IOException e) {
            log.error(String.format("Caught IOException during registration of %s", e.getLocalizedMessage()));
        }
    }

    @Operation(description = "Request a new token.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(Authentication authentication) {
        String token = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new TokenResponse(token, "Bearer"), HttpStatus.OK);
    }

    @Operation(description = "Login.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch(AuthenticationException e) {
            log.warn("Authentication failed for: " + loginRequest.getUsername());
            throw new GeneralException("Authentication Failed", "", HttpStatus.FORBIDDEN);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        log.debug(String.format("Login successful for %s", userDetails.getUsername()));
        return new ResponseEntity<>(new TokenResponse(jwt, "Bearer"), HttpStatus.OK);
    }

    @Operation(description = "Register new user.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "309", description = "Conflict, generally email is already registered.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest, Errors errors) {
        if (!registrationEnabled) {
            log.warn(String.format("Registration attempted with email: %s, while registrations are disabled.", registerRequest.getEmail()));
            throw new GeneralException("Not found", "", HttpStatus.NOT_FOUND);
        }
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        authService.register(registerRequest);
        log.info(String.format("User Registered (email='%s')", registerRequest.getEmail()));
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
}
