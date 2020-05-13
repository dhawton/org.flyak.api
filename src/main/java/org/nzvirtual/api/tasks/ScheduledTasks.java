package org.nzvirtual.api.tasks;

import org.nzvirtual.api.data.entity.PasswordReset;
import org.nzvirtual.api.data.entity.RefreshToken;
import org.nzvirtual.api.data.repository.PasswordResetRepository;
import org.nzvirtual.api.data.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
class ScheduledTasks {
    private RefreshTokenRepository refreshTokenRepository;
    private PasswordResetRepository passwordResetRepository;

    public ScheduledTasks(RefreshTokenRepository refreshTokenRepository, PasswordResetRepository passwordResetRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetRepository = passwordResetRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    public void runTasks() {
        checkExpiredTokens();
        checkExpiredResetTokens();
    }

    @Async
    @Transactional
    void checkExpiredTokens() {
        LocalDate date = LocalDate.now().plusWeeks(2);
        List<RefreshToken> refreshTokens = refreshTokenRepository.findExpiredTokens(date);
        refreshTokenRepository.deleteAll(refreshTokens);
    }

    @Async
    @Transactional
    void checkExpiredResetTokens() {
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(15);
        List<PasswordReset> refreshTokens = passwordResetRepository.findExpiredTokens(dateTime);
        passwordResetRepository.deleteAll(refreshTokens);
    }
}
