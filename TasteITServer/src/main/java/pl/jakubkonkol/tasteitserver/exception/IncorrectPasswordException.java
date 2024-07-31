package pl.jakubkonkol.tasteitserver.exception;

import org.springframework.web.server.ResponseStatusException;

public class IncorrectPasswordException extends ResponseStatusException {
    public IncorrectPasswordException(String message) {
        super(org.springframework.http.HttpStatus.UNAUTHORIZED, message);
    }
}
