package com.server.db.controller.handler;

import com.server.db.Tools;
import com.server.db.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> onValidationException(final ValidationException validationException) {
        return new ResponseEntity<>(Tools.errorsToResponse(validationException.getBindingResult()), HttpStatus.BAD_REQUEST);
    }
}
