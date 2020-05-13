package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.PasswordReset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);

    @Query("SELECT r FROM password_resets r WHERE created_at < :expiredDate")
    List<PasswordReset> findExpiredTokens(@Param("expiredDate") LocalDateTime expiredDate);
}
