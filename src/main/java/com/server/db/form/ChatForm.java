package com.server.db.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatForm {
    @NotNull
    private String[] memberName;
}
