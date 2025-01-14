package pl.jakubkonkol.tasteitserver.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.jakubkonkol.tasteitserver.exception.ResourceNotFoundException;
import pl.jakubkonkol.tasteitserver.model.ErrorResponse;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return buildErrorResponse(e, 500);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(e, 404);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(IllegalArgumentException e) {
        return buildErrorResponse(e, 400);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(IllegalStateException e) {
        return buildErrorResponse(e, 409);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {
        return buildErrorResponse(e, 400);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, int status) {
        ErrorResponse errorResponse = new ErrorResponse(status, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(status));
    }
}