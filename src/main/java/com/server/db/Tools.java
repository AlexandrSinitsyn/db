package com.server.db;

import com.server.db.domain.User;
import org.springframework.validation.BindingResult;

public final class Tools {
    public static final int SYSTEM_USER_ID = 0;
    public static final String SUCCESS_RESPONSE = "success";

    public static String errorsToResponse(final BindingResult bindingResult) {
        return bindingResult.toString();
    }

    public static String userToJsonString(final User user) {
        return user.getLogin();
    }
}
