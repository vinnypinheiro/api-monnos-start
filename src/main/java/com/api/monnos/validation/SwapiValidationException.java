package com.api.monnos.validation;

import org.springframework.http.HttpStatus;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
public class SwapiValidationException extends Exception {

    private HttpStatus httpStatus;

    public SwapiValidationException() {
        super();
    }

    public SwapiValidationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
