package com.unistore.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class StandardException extends Exception {

    private static final long serialVersionUID = -5190796526753119931L;

    private HttpStatus httpStatus;

    private List<StandardError> standardError;

    private StandardErrorCode standardErrorCode;

    private Throwable cause;

    public StandardException(HttpStatus httpStatus, List<StandardError> standardError,
            StandardErrorCode standardErrorCode, Throwable cause) {
        super();
        this.httpStatus = httpStatus;
        this.standardErrorCode = standardErrorCode;
        this.standardError = standardError;
        this.cause = cause;
    }

}
