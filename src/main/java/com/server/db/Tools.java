package com.server.db;

import com.server.db.domain.User;
import org.springframework.validation.BindingResult;

public final class Tools {
    public static final int SYSTEM_USER_ID = 1;
    public static final int VISITOR_ID = -2;
    public static final String SUCCESS_RESPONSE = "success";
    public static final String USER_ID_KEY = "userId";

    public static String errorsToResponse(final BindingResult bindingResult) {
        return bindingResult.toString();
    }

    public static String userToJsonString(final User user) {
        return user.getLogin();
    }
}
