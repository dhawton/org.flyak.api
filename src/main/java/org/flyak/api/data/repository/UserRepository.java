package org.flyak.api.data.repository;

import org.flyak.api.data.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String verification_token);
    Boolean existsByEmail(String email);
}
