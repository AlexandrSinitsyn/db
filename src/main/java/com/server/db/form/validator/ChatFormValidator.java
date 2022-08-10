package com.server.db.form.validator;

import com.server.db.form.ChatForm;
import com.server.db.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChatFormValidator implements Validator {
    private final ChatService chatService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return ChatForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        if (!errors.hasErrors()) {
            final ChatForm registerForm = (ChatForm) target;
        }
    }
}
