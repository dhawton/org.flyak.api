package org.nzvirtual.api.exception;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {
    private final Errors errors;

    public ValidationException(Errors errors) {
        super("Validation Exception");
        this.errors = errors;
    }

    public Errors getErrors() {
        return this.errors;
    }
}
