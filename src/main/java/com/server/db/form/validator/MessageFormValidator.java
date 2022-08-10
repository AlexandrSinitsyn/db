package com.server.db.form.validator;

import com.server.db.form.MessageForm;
import com.server.db.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MessageFormValidator implements Validator {
    private final MessageService messageService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return MessageForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        if (!errors.hasErrors()) {
            final MessageForm registerForm = (MessageForm) target;
        }
    }
}
