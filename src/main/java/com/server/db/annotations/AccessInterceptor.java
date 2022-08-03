package com.server.db.annotations;

import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private static final String USER_ID_KEY = "userId";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (handler instanceof final HandlerMethod handlerMethod) {
            final Method method = handlerMethod.getMethod();

            final List<Class<? extends Annotation>> access = Arrays.stream(method.getDeclaredAnnotations())
                    .filter(a -> a.annotationType().getAnnotation(Access.class) != null)
                    .<Class<? extends Annotation>>map(Annotation::annotationType).toList();
            if (access.isEmpty()) {
                return true;
            }

            if (access.contains(NoOuterAccess.class)) {
                response.sendRedirect("/accessDenied");
                return false;
            }

            if (access.contains(PrivateOnly.class)) {
                final User user = userService.findById((Long) request.getSession().getAttribute(USER_ID_KEY));

                if (user.getId() == getUserIdFromRequest(request)) {
                    return true;
                }

                response.sendRedirect("/accessDenied");
                return false;
            }

        }

        return true;
    }

    private long getUserIdFromRequest(final HttpServletRequest request) {
        try {
            final UserForm userForm = new UserForm();
            userForm.setLogin(Objects.requireNonNull(request.getParameter("login")));
            userForm.setPassword(Objects.requireNonNull(request.getParameter("password")));
            return userForm.toUser(userService).getId();
        } catch (final NullPointerException e) {
            return -1;
        }
    }
}
