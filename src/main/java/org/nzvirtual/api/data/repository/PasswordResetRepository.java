package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.PasswordReset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);

    @Query("SELECT p FROM PasswordReset p WHERE p.created_at < :expiredDate")
    List<PasswordReset> findExpiredTokens(@Param("expiredDate") Date expiredDate);
}
