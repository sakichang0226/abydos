package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.ErrorMessage;

public class RequestInvalidException extends CommonWarnException {

    public RequestInvalidException(ErrorMessage message, Object... objects) {
        super(RequestInvalidException.class, message, objects);
    }

}
