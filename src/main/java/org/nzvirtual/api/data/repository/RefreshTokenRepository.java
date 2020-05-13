package org.nzvirtual.api.data.repository;

import org.nzvirtual.api.data.entity.RefreshToken;
import org.nzvirtual.api.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    long deleteByUser(User user);

    @Query("SELECT r FROM refreshtokens r WHERE created_at < :expiredDate")
    List<RefreshToken> findExpiredTokens(@Param("expiredDate") LocalDate expiredDate);
}
