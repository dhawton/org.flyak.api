package org.flyak.api.controllers;

import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.dto.GeneralStatusResponse;
import org.flyak.api.dto.UserRequest;
import org.flyak.api.exception.GeneralException;
import org.flyak.api.service.UserService;
import org.flyak.api.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public Optional<User> getUser(Principal principal) {
        String email = principal.getName();
        return this.userRepository.findByEmail(email);
    }

    @GetMapping("/{userId}")
    public Optional<User> getUser(@PathVariable Long userId, Principal principal) {
        return this.userRepository.findById(userId);
    }

    @PutMapping()
    public ResponseEntity<GeneralStatusResponse> putUser(@RequestBody UserRequest newUser, Principal principal) {
        boolean passwordChanged = false;
        Optional<User> optionalUser = this.userRepository.findByEmail(principal.getName());
        if (!optionalUser.isPresent()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.changeUser(user.getId(), newUser);
        log.info(String.format("User %s (%s) updated their profile.", user.getId(), user.getEmail()));

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<GeneralStatusResponse> putUserAdmin(@PathVariable Long userId, @RequestBody UserRequest newUser, Principal principal) {
        boolean passwordChanged = false;
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        userService.changeUser(userId, newUser);
        log.info(String.format("User %s (%s) updated by %s", user.getId(), user.getEmail(), principal.getName()));

        if (newUser.isGenpassword()) {
            String newPassword = PasswordGenerator.generateRandomPassword(12);
            userService.changePassword(user, newPassword);
            log.info(String.format("User %s's password reset by %s", user.getId(), principal.getName()));
            passwordChanged = true;
            // @TODO This should get emailed to the pilot
        }

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<GeneralStatusResponse> deleteUserAdmin(@PathVariable Long userId, Principal principal) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new GeneralException("User not found", "", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        log.info(String.format("User %s (%s) deleted by %s", user.getId(), user.getEmail(), principal.getName()));
        userRepository.delete(user);

        return new ResponseEntity<>(new GeneralStatusResponse("OK"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }
}
