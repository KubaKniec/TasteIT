package mixitserver.exception;

public class ApiDataParsingRuntimeException extends RuntimeException {
    public ApiDataParsingRuntimeException() {
        super();
    }

    public ApiDataParsingRuntimeException(String message) {
        super(message);
    }

    public ApiDataParsingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

