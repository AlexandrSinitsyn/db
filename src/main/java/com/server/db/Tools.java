package com.server.db;

import com.server.db.domain.User;
import com.server.db.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public final class Tools {
    public static final long SYSTEM_USER_ID = 1;
    public static final int VISITOR_ID = -2;
    public static final String SUCCESS_RESPONSE = "success";
    public static final String USER_ID_KEY = "userId";
    public static final String SYSTEM_PARAMETER = "SYSTEM";
    public static final String SYSTEM_ID_PARAMETER = "SYSTEM_ID";

    public static final boolean DEBUG = false;
    public static final String JWT_KEY = "jwt-secret";

    public static final Properties PROPERTIES;

    static {
        try {
            final Path path = Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI());

            PROPERTIES = new Properties();
            PROPERTIES.load(new FileInputStream(path.resolve("private-keys.properties").toString()));
        } catch (final IOException | URISyntaxException e) {
            throw new RuntimeException("NO SECRET FOUND!", e);
        }
    }

    public static User SESSION_USER = null;

    public static String errorsToResponse(final BindingResult bindingResult) {
        final ObjectError error = bindingResult.getAllErrors().get(0);

        if (error instanceof final FieldError fieldError) {
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }

        return error.getDefaultMessage();
    }

    public static User getUserFromSession(final HttpSession session, final UserService userService) {
        return userService.findById((Long) session.getAttribute(Tools.USER_ID_KEY));
    }
}
