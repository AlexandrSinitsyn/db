package com.server.db.form.validator;

import com.server.db.form.UserForm;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserFormValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        if (!errors.hasErrors()) {
            final UserForm registerForm = (UserForm) target;

            // if (!userService.isLoginVacant(registerForm.getLogin())) {
            //     errors.rejectValue("login", "login.is-in-use", "login is in use already");
            // }
        }
    }
}
