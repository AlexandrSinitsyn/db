package com.server.db.annotations;

import com.server.db.DbApplication;
import com.server.db.Tools;
import com.server.db.controller.ConnectionController;
import com.server.db.controller.ErrorController;
import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
    // private final UserService userService;
    //
    // @Override
    // public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
    //     if (Tools.DEBUG) {
    //         request.getSession().setAttribute(Tools.USER_ID_KEY, Tools.SYSTEM_USER_ID);
    //     }
    //
    //     if (handler instanceof final HandlerMethod handlerMethod) {
    //         final Method method = handlerMethod.getMethod();
    //
    //         final Class<?> clazz = method.getDeclaringClass();
    //         if (!clazz.getPackageName().startsWith(DbApplication.class.getPackageName()) ||
    //                 method.getDeclaringClass().isAssignableFrom(ErrorController.class) ||
    //                 method.getDeclaringClass().isAssignableFrom(ConnectionController.class)) {
    //             return true;
    //         }
    //
    //         final List<Class<? extends Annotation>> access = Arrays.stream(method.getDeclaredAnnotations())
    //                 .filter(a -> a.annotationType().getAnnotation(Access.class) != null)
    //                 .<Class<? extends Annotation>>map(Annotation::annotationType).toList();
    //
    //         if (access.contains(NoOuterAccess.class)) {
    //             response.sendRedirect("/accessDenied");
    //             return false;
    //         }
    //
    //         if (access.contains(Any.class)) {
    //             return true;
    //         }
    //
    //         if (request.getSession().getAttribute(Tools.USER_ID_KEY) == null) {
    //             response.sendRedirect("/noUserId");
    //             return false;
    //         }
    //
    //         ///
    //         Tools.SESSION_USER = Tools.getUserFromSession(request.getSession(), userService);
    //         ///
    //
    //         final User user = Tools.getUserFromSession(request.getSession(), userService);
    //
    //         if (user == null ||
    //                 (access.contains(SystemOnly.class) && request.getParameter(Tools.SYSTEM_PARAMETER) != null &&
    //                         Objects.equals(request.getParameter(Tools.SYSTEM_ID_PARAMETER), Tools.SYSTEM_USER_ID + "")) ||
    //                 (access.contains(Admin.class) && !user.isAdmin())/* ||
    //                 (access.contains(PrivateOnly.class) && user.getId() != getUserIdFromRequest(request))*/) {
    //             response.sendRedirect("/accessDenied");
    //             return false;
    //         }
    //     }
    //
    //     return true;
    // }
    //
    // @Override
    // public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
    //     HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    //
    //     Tools.SESSION_USER = null;
    // }
    //
    // private long getUserIdFromRequest(final HttpServletRequest request) {
    //     try {
    //         final UserForm userForm = new UserForm();
    //         userForm.setLogin(Objects.requireNonNull(request.getParameter("login")));
    //         userForm.setPassword(Objects.requireNonNull(request.getParameter("password")));
    //         return userForm.toUser(userService).getId();
    //     } catch (final NullPointerException e) {
    //         return -1;
    //     }
    // }
}
