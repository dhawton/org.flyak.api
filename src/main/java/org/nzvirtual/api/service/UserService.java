package org.nzvirtual.api.service;

import org.nzvirtual.api.data.entity.User;
import org.nzvirtual.api.data.repository.UserRepository;
import org.nzvirtual.api.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void changeUser(Long id, UserRequest newUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(newUser.getEmail());
            user.setName(newUser.getName());
            if (newUser.isGenpassword()) {
                this.changePassword(user, newUser.getNewpassword());
            }
            if (newUser.getNewpassword() != null && !newUser.isGenpassword()) {
                if (BCrypt.checkpw(newUser.getPassword(), user.getPassword())) {
                    this.changePassword(user, newUser.getNewpassword());
                }
            }
            userRepository.save(user);
        }
    }

    @Transactional
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
