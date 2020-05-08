package org.flyak.api.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {
    private final String details;
    private final HttpStatus code;

    public GeneralException(String message, String details, HttpStatus code) {
        super(message);
        this.details = details;
        this.code = code;
    }
    public GeneralException(String details, HttpStatus code) {
        super("General Exception");
        this.details = details;
        this.code = code;
    }

    public String getDetails() { return this.details; }
    public HttpStatus getCode() { return this.code != null ? this.code : HttpStatus.INTERNAL_SERVER_ERROR; }
}
