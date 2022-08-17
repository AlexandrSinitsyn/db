package com.server.db.form;

import com.server.db.domain.User;
import com.server.db.service.UserService;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserForm {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String login;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String password;

    public User toUser(final String name) {
        final var user = new User();
        user.setLogin(login);
        user.setName(name);
        return user;
    }

    public User toUser(final UserService userService) {
        return userService.findByLoginAndPassword(login, password);
    }
}
