package com.hasim.genesis.api.exception;

import com.hasim.genesis.api.model.ErrorResponse;

public class ApplicationDownstreamException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorResponse validationErrorResponse;

    public ApplicationDownstreamException() {
        super();
    }

    public ApplicationDownstreamException(String message) {
        super(message);
    }

    public ApplicationDownstreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationDownstreamException(Throwable cause) {
        super(cause);
    }

    public void setValidationErrorResponse(ErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;

    }

    public ErrorResponse getValidationErrorResponse() {
        return validationErrorResponse;

    }
}
