package com.server.db;

import com.server.db.domain.User;
import com.server.db.service.UserService;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;

public final class Tools {
    public static final long SYSTEM_USER_ID = 1;
    public static final int VISITOR_ID = -2;
    public static final String SUCCESS_RESPONSE = "success";
    public static final String USER_ID_KEY = "userId";

    public static final boolean DEBUG = true;

    public static User SESSION_USER = null;

    public static String errorsToResponse(final BindingResult bindingResult) {
        return bindingResult.toString();
    }

    public static User getUserFromSession(final HttpSession session, final UserService userService) {
        return userService.findById((Long) session.getAttribute(Tools.USER_ID_KEY));
    }
}
