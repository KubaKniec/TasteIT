package pl.jakubkonkol.tasteitserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountAlreadyExistsException extends ResponseStatusException {
    public AccountAlreadyExistsException(String message) { super(HttpStatus.CONFLICT, message);}
}
