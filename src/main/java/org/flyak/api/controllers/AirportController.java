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
public class AirportController {


    public AirportController(AuthService authService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailService emailService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
        this.emailService = emailService;
    }

}
