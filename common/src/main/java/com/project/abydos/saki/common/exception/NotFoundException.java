package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.ErrorMessage;

public class NotFoundException extends CommonWarnException {

    public NotFoundException(ErrorMessage message, Object... objects) {
        super(NotFoundException.class, message, objects);
    }

}
