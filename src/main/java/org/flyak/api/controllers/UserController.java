package org.flyak.api.controllers;

import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.flyak.api.exception.GeneralException;
import org.flyak.api.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping(value = { "/{userId}", "/" })
    public void getUser(@PathVariable Optional<String> userId, Principal principal) {
        long id = 0;
        if (userId.isPresent()) {
            id = Long.parseLong(String.valueOf(userId));
        } else {
            if (principal instanceof UserDetails) {
                id = ((UserDetailsImpl)principal).getId();
            }
        }

        if (id == 0) {
            log.error("getUser could not find user id");
            throw new GeneralException("Not found", "", HttpStatus.NOT_FOUND);
        }


    }
}
