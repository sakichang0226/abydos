package com.project.abydos.saki.common.constant;

/**
 *
 */
public enum ErrorMessage {

    INTERNAL_SERVER_ERROR("internal Server Error."),
    VALIDATION_ERROR("{0} validation Check Error."),
    DATA_NOT_FOUND("{0} not found.");

    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
