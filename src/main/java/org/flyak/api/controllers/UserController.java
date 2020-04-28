package org.flyak.api.controllers;

import com.sun.mail.iap.Response;
import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.dto.PutUserResponse;
import org.flyak.api.dto.UserRequest;
import org.flyak.api.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

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
    public ResponseEntity<PutUserResponse> putUser(@RequestBody UserRequest newUser, Principal principal) {
        boolean passwordChanged = false;
        User user = this.userRepository.findByEmail(principal.getName());
        user.setEmail(newUser.getEmail());
        user.setName(newUser.getName());
        if (newUser.getNewpassword() != null) {
            if (BCrypt.checkpw(newUser.getPassword(), user.getPassword())) {
                user.setPassword(newUser.getPassword());
                passwordChanged = true;
            }
        }
        userRepository.save(user);

        return new ResponseEntity<>(new PutUserResponse("OK", passwordChanged), HttpStatus.CREATED);
    }

    @PutMapping()
    public String put

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }
}
