package pl.jakubkonkol.tasteitserver.exception;

import org.springframework.web.server.ResponseStatusException;

public class AccountDoesNotExistException extends ResponseStatusException {
    public AccountDoesNotExistException(String message) {
        super(org.springframework.http.HttpStatus.NOT_FOUND, message);
    }
}
