package com.server.db.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatForm {
    @NotNull
    @NotBlank
    private String jwt;

    @NotNull
    private String[] memberName;
}
