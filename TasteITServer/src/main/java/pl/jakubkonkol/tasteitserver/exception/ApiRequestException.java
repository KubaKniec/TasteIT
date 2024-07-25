package pl.jakubkonkol.tasteitserver.exception;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException(String message) {
        super(message);
    }

}
