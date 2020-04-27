package org.flyak.api.controllers;

import org.flyak.api.dto.LoginRequest;
import org.flyak.api.dto.RegisterRequest;
import org.flyak.api.dto.TokenResponse;
import org.flyak.api.exception.GeneralException;
import org.flyak.api.exception.ValidationException;
import org.flyak.api.security.JwtUtils;
import org.flyak.api.security.UserDetailsImpl;
import org.flyak.api.service.AuthService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Value("${app.registration.enabled}")
    private Boolean registrationEnabled;
    private Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(Authentication authentication) {
        String token = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(new TokenResponse(token, "Bearer"), HttpStatus.OK);
    }

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
