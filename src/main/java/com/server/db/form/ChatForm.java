package com.server.db.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChatForm {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]+")
    private String adminName;

    private String[] memberName;
}
