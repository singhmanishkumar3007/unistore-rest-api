package com.unistore.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class UnistoreResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnistoreException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(UnistoreException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),  ex.getStandardErrorCode(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails,
                ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(Exception.class)
    // public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex,
    // WebRequest request) {
    // ErrorDetails errorDetails = new ErrorDetails(new Date(), new ArrayList<>(),
    // StandardErrorCode.SC500,
    // request.getDescription(false));
    // return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    // }

}
