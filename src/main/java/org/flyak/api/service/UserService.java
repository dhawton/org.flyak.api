package org.flyak.api.service;

import org.flyak.api.data.entity.User;
import org.flyak.api.data.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void getUser() {}
}
