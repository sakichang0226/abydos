package com.project.abydos.saki.common.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(RuntimeException exception){
        log.warn(exception.getMessage());

        return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
