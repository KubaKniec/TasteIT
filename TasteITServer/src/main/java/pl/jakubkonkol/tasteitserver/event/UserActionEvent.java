package pl.jakubkonkol.tasteitserver.event;

import lombok.Getter;
import pl.jakubkonkol.tasteitserver.model.UserAction;

import java.time.LocalDateTime;

@Getter
public record UserActionEvent(String userId, UserAction action, LocalDateTime timestamp) {
    public UserActionEvent(String userId, UserAction action, LocalDateTime timestamp) {
        this.userId = userId;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }
}
