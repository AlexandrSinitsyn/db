package com.server.db.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
@Getter
public final class ValidationException extends RuntimeException {
    private final BindingResult bindingResult;
}
