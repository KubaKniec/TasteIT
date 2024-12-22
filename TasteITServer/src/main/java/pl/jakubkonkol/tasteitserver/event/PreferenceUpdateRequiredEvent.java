package pl.jakubkonkol.tasteitserver.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PreferenceUpdateRequiredEvent {
    private final String userId;
    private final LocalDateTime timestamp;
    private final String reason; //enum?

    public PreferenceUpdateRequiredEvent(String userId, String reason) {
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
    }
}
