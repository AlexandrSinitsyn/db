package com.server.db.form.validator;

import com.server.db.form.UserUpdateForm;
import com.server.db.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserUpdateFormValidator implements Validator {
    private final JwtService jwtService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserUpdateForm.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        if (!errors.hasErrors()) {
            final UserUpdateForm form = (UserUpdateForm) target;

            if (jwtService.findUser(form.getJwt()) == null) {
                errors.rejectValue("invalid-jwt", "Unknown user's identification key");
            }
        }
    }
}
