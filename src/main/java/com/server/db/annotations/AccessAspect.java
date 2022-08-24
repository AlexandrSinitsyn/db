package com.server.db.annotations;

import com.server.db.Tools;
import com.server.db.domain.Chat;
import com.server.db.domain.Message;
import com.server.db.domain.User;
import com.server.db.form.MessageForm;
import com.server.db.form.UserUpdateForm;
import com.server.db.service.ChatService;
import com.server.db.service.MessageService;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessAspect {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;
    private final Set<Long> awaitUsers = ConcurrentHashMap.newKeySet();

    @Pointcut("@annotation(confirmation)")
    public void awaitConfirmation(final Confirmation confirmation) {}

    @SuppressWarnings("ArgNamesWarningsInspection")
    @Around("awaitConfirmation(confirmation)")
    public Object handlerConfirmation(final ProceedingJoinPoint joinPoint, final Confirmation confirmation)
            throws Throwable {
        final User user = (User) joinPoint.getArgs()[0];

        if (user == null) {
            return "unknown user";
        }

        if (awaitUsers.contains(user.getId())) {
            switch (confirmation.value()) {
                // case "password" -> {
                //     awaitUsers.remove(user.getId());
                //
                //     if (userService.findByLoginAndPassword(user.getLogin(), (String) user.getAttached()).join() == user) {
                //         return joinPoint.proceed();
                //     }
                // }
                case "action" -> {
                    return joinPoint.proceed();
                }
                default -> throw new IllegalArgumentException("Unknown confirmation request");
            }
        }

        awaitUsers.add(user.getId());

        return switch (confirmation.value()) {
            // case "password" -> "password";
            case "action" -> "confirm";
            default -> throw new IllegalArgumentException("Unknown confirmation request");
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Around("@annotation(Admin)")
    public Object handlerAdmin(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AccessInterceptor.sUser.isValid() ||
            !AccessInterceptor.sUser.getUser().isAdmin()) {
            return null;
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(Logined)")
    public Object handlerLogined(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AccessInterceptor.sUser.isValid()) {
            return null;
        }

        return joinPoint.proceed();
    }

    @SuppressWarnings("ConstantConditions")
    @Around("@annotation(SystemOnly)")
    public Object handlerSystemOnly(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AccessInterceptor.sUser.isValid() &&
                AccessInterceptor.sUser.getUser().getId() == Tools.SYSTEM_USER_ID) {
            return null;
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(ChatMember)")
    public Object handlerChatMember(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AccessInterceptor.sUser.isValid()) {
            return null;
        }

        final long id = (long) joinPoint.getArgs()[0];
        final Chat chat = chatService.findById(id);

        if (!chat.checkPrivacy(AccessInterceptor.sUser.getUser())) {
            return null;
        }

        return joinPoint.proceed();
    }

    @SuppressWarnings("ConstantConditions")
    @Around("@annotation(PrivateOnly)")
    public Object handlerPrivateOnly(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AccessInterceptor.sUser.isValid()) {
            return null;
        }

        if (joinPoint.getArgs().length < 1) {
            return null;
        }

        if (joinPoint.getArgs()[0] instanceof final UserUpdateForm userUpdateForm) {
            final String password = userUpdateForm.getPassword();

            final User found = userService.findByLoginAndPassword(AccessInterceptor.sUser.getUser().getLogin(), password).join();
            if (found != null && found.getId() == AccessInterceptor.sUser.getUser().getId()) {
                return joinPoint.proceed();
            }
        }

        if (joinPoint.getArgs().length < 2) {
            return null;
        }

        if (joinPoint.getArgs()[1] instanceof MessageForm) {
            final long id = (long) joinPoint.getArgs()[0];

            final Message found = messageService.findById(id);

            if (found != null && found.checkPrivacy(AccessInterceptor.sUser.getUser())) {
                return joinPoint.proceed();
            }
        }

        return null;
    }
}
