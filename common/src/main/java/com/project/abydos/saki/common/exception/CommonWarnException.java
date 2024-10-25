package com.project.abydos.saki.common.exception;


import com.project.abydos.saki.common.constant.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

@Slf4j
public abstract class CommonWarnException extends CommonProjectException{

    public <T extends CommonProjectException> CommonWarnException(
            Class<T> clazz, ErrorMessage message, Object... objects) {
        super(MessageFormat.format(message.getMessage(), objects));
        log.warn(clazz.getName() + ":" + MessageFormat.format(message.getMessage(), objects));
    }


}
