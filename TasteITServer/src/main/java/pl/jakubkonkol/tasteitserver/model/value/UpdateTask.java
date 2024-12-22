package pl.jakubkonkol.tasteitserver.model.value;

import lombok.Value;

import java.time.LocalDateTime;

public record UpdateTask(
        String userId,
        String reason,
        LocalDateTime createdAt
) {
    public UpdateTask(String userId, String reason) {
        this(userId, reason, LocalDateTime.now());
    }
}