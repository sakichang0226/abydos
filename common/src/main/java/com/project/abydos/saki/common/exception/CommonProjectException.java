package com.project.abydos.saki.common.exception;

public abstract class CommonProjectException extends RuntimeException{

    public CommonProjectException(String message) {
        super(message);
    }

    public CommonProjectException(String message, Throwable cause) {
        super(message);
    }

}
