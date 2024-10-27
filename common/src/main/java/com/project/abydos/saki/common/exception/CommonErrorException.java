package com.project.abydos.saki.common.exception;


import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

@Slf4j
public class CommonErrorException extends CommonProjectException{

    public <T extends CommonProjectException> CommonErrorException(
            Class<T> clazz, Error message, Object... objects) {
        super(MessageFormat.format(message.getMessage(), objects));
        log.error(clazz.getName() + ":" + MessageFormat.format(message.getMessage(), objects));
    }

}
