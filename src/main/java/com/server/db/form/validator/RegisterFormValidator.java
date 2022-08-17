package com.server.db.form.validator;

import com.server.db.form.RegisterForm;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class RegisterFormValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return RegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        if (!errors.hasErrors()) {
            final RegisterForm registerForm = (RegisterForm) target;

            if (!userService.isLoginVacant(registerForm.getLogin())) {
                errors.rejectValue("login", "is-already-in-use");
            }
        }
    }
}
