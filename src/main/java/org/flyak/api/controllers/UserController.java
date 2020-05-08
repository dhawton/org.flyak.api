package org.flyak.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.dto.GeneralStatusResponse;
import org.flyak.api.dto.UserRequest;
import org.flyak.api.exception.GeneralException;
import org.flyak.api.service.EmailService;
import org.flyak.api.service.UserService;
import org.flyak.api.utils.PasswordGenerator;
import org.flyak.api.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final TokenGenerator tokenGenerator;
    @Value("${app.ui.baseurl}")
    private String UIBaseURL;
    @Value("${app.ui.registration_verification}")
    private String UIRegVerificationURL;

    public UserController(UserRepository userRepository, UserService userService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.tokenGenerator = new TokenGenerator(12, new SecureRandom());
    }

    @Operation(description = "Get authenticated user data.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping()
    public Optional<User> getUser(Principal principal) {
        String email = principal.getName();
        return this.userRepository.findByEmail(email);
    }

    @Operation(tags = { "admin" }, description = "Update {userId} user.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
         Optional<User> user = this.userRepository.findById(userId);
         if (user.isEmpty())
             throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);

         return user.get();
    }

    @Operation(description = "Update authenticated user.", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = GeneralStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @PutMapping()
    public ResponseEntity<GeneralStatusResponse> putUser(@RequestBody UserRequest newUser, Principal principal) {
        Optional<User> optionalUser = this.userRepository.findByEmail(principal.getName());
        if (optionalUser.isEmpty()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.changeUser(user.getId(), newUser);
        log.info(String.format("User %s (%s) updated their profile.", user.getId(), user.getEmail()));

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.CREATED);
    }

    @Operation(tags = { "admin" }, description = "Update {userId} user.", responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = GeneralStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @PutMapping("/{userId}")
    public ResponseEntity<GeneralStatusResponse> putUserAdmin(@PathVariable Long userId, @RequestBody UserRequest newUser, Principal principal) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.changeUser(userId, newUser);
        log.info(String.format("User %s (%s) updated by %s", user.getId(), user.getEmail(), principal.getName()));

        if (newUser.isGenpassword()) {
            String newPassword = PasswordGenerator.generateRandomPassword(12);
            userService.changePassword(user, newPassword);
            log.info(String.format("User %s's password reset by %s", user.getId(), principal.getName()));
            Map<String, Object> props = new HashMap<>();
            props.put("name", user.getName());
            props.put("newpassword", newPassword);
            emailService.generateEmailRequest(user.getEmail(), "Password has been reset", "email-passwordreset", props);
        }

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.CREATED);
    }

    @Operation(tags = { "admin" }, description = "Delete {userId} user.", responses = {
            @ApiResponse(responseCode = "202", content = @Content(schema = @Schema(implementation = GeneralStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<GeneralStatusResponse> deleteUserAdmin(@PathVariable Long userId, Principal principal) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        log.info(String.format("User %s (%s) deleted by %s", user.getId(), user.getEmail(), principal.getName()));
        userRepository.delete(user);

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.ACCEPTED);
    }

    @Operation(tags = { "admin" }, description = "Get list of users.", responses = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(arraySchema = @Schema(implementation = GeneralStatusResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping("/all")
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }
}
