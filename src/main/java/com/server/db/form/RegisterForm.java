package com.server.db.form;

import com.server.db.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterForm {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String login;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String password;

    public User toUser() {
        final var user = new User();
        user.setLogin(login);
        user.setName(name);
        return user;
    }
}
