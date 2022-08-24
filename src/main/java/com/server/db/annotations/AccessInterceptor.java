package com.server.db.annotations;

import com.server.db.DbApplication;
import com.server.db.domain.User;
import com.server.db.service.JwtService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    public static final class SessionUser {
        @Setter(AccessLevel.PRIVATE)
        private User user;
        @Getter
        @Setter(AccessLevel.PRIVATE)
        private boolean valid;

        public User getUser() {
            return isValid() ? user : null;
        }
    }
    public static final SessionUser sUser = new SessionUser();

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (handler instanceof final HandlerMethod handlerMethod) {
            final Method method = handlerMethod.getMethod();

            final Class<?> clazz = method.getDeclaringClass();
            if (!clazz.getPackageName().startsWith(DbApplication.class.getPackageName())) {
                return true;
            }

            if (method.getDeclaredAnnotation(NoOuterAccess.class) != null) {
                return false;
            }

            if (method.getDeclaredAnnotation(Any.class) != null) {
                return true;
            }
        }

        sUser.setValid(false);

        final String header = request.getHeader("Authorization");
        if (header != null) {
            final String jwt = header.split("Bearer ")[1];
            sUser.setUser(jwtService.findUser(jwt));
            sUser.setValid(true);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
