package com.server.db.annotations;

import com.server.db.Tools;
import com.server.db.domain.DbEntity;
import com.server.db.domain.User;
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
    private final UserService userService;
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
                case "password" -> {
                    awaitUsers.remove(user.getId());

                    if (userService.findByLoginAndPassword(user.getLogin(), (String) user.getAttached()).join() == user) {
                        return joinPoint.proceed();
                    }
                }
                case "action" -> {
                    return joinPoint.proceed();
                }
                default -> throw new IllegalArgumentException("Unknown confirmation request");
            }
        }

        switch (confirmation.value()) {
            case "password" -> {
                awaitUsers.add(user.getId());

                return "password";
            }
            case "action" -> {
                return "confirm";
            }
            default -> throw new IllegalArgumentException("Unknown confirmation request");
        }
    }

    @Around("@annotation(PrivateOnly)")
    public Object handlerPrivateOnly(final ProceedingJoinPoint joinPoint) throws Throwable {
        final DbEntity entity = (DbEntity) joinPoint.getArgs()[0];

        if (entity.checkPrivacy(Tools.SESSION_USER)) {
            return joinPoint.proceed();
        } else {
            return "redirect:/accessDenied";
        }
    }
}
