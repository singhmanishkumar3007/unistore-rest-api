package com.unistore.exception;

import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class UnistoreException extends Exception {

  private static final long serialVersionUID = -5190796526753119931L;

  private HttpStatus httpStatus;


  private UnistoreErrorCode standardErrorCode;

  private String message;

  private Throwable cause;

  public UnistoreException(HttpStatus httpStatus, UnistoreErrorCode standardErrorCode,
      Throwable cause) {
    super();
    this.httpStatus = httpStatus;
    this.standardErrorCode = standardErrorCode;
    this.cause = cause;
  }

  public UnistoreException(HttpStatus httpStatus, UnistoreErrorCode standardErrorCode,
      String message, Throwable cause) {
    super();
    this.httpStatus = httpStatus;
    this.standardErrorCode = standardErrorCode;
    this.message = message;
    this.cause = cause;
  }

}
