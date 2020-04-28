package org.flyak.api.controllers;

import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }
}
