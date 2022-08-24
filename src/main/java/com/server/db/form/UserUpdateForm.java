package com.server.db.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserUpdateForm {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
    private String password;

    @NotNull
    @NotBlank
    private String attachment;
}
