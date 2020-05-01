package org.flyak.api.data.repository;

import org.flyak.api.data.entity.PasswordReset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
}
