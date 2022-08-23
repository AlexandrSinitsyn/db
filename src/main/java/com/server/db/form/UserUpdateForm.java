package com.server.db.form;

import com.server.db.domain.User;
import com.server.db.service.JwtService;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserUpdateForm {
    @NotNull
    @NotBlank
    private String jwt;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String password;

    @NotNull
    @NotBlank
    private String attachment;

    public User toUser(final JwtService jwtService) {
        return jwtService.findUser(jwt);
    }
}
