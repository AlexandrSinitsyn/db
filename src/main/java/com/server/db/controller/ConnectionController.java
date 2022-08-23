package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.domain.User;
import com.server.db.exceptions.ValidationException;
import com.server.db.form.RegisterForm;
import com.server.db.form.UserForm;
import com.server.db.form.validator.RegisterFormValidator;
import com.server.db.service.JwtService;
import com.server.db.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class ConnectionController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RegisterFormValidator registerFormValidator;

    @InitBinder("registerForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(registerFormValidator);
    }

    @PostMapping("/registerUser")
    @Deprecated(forRemoval = true)
    public User register(@Valid @ModelAttribute("registerForm") final RegisterForm registerForm,
                         final BindingResult bindingResult,
                         final HttpSession session) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        final User user = userService.save(registerForm.toUser()).join();
        userService.updatePasswordSha(user, registerForm.getPassword());

        session.setAttribute(Tools.USER_ID_KEY, user.getId());

        return user;
    }

    @PostMapping("register")
    public String register(@Valid @RequestBody final RegisterForm registerForm, final BindingResult bindingResult) {
        if (userService.findByLogin(registerForm.getLogin()) != null) {
            bindingResult.addError(new ObjectError("invalid-login", "login is already occupied"));
        }

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final User user = userService.save(registerForm.toUser()).join();
        userService.updatePasswordSha(user, registerForm.getPassword());

        return jwtService.create(user);
    }

    @PutMapping("/connect")
    @Operation(summary = "Provides connection to server. And sets this user 'id' to session",
            description = "Send 'visitor=true' if this user is just a visitor or 'login=...&password=...' in the opposite case")
    @Deprecated(forRemoval = true)
    public String connect(@RequestParam(required = false) final String visitor,
                          @RequestParam(required = false) final String login,
                          @RequestParam(required = false) final String password,
                          final HttpSession session) {
        final User user;
        if (visitor != null) {
            user = new User();
            user.setId(Tools.VISITOR_ID);
        } else {
            user = userService.findByLoginAndPassword(login, password).join();
        }

        session.setAttribute(Tools.USER_ID_KEY, user.getId());

        return Tools.SUCCESS_RESPONSE;
    }

    @GetMapping("users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.findUser(jwt);
    }

    @GetMapping("/logIn")
    @Deprecated(forRemoval = true)
    public CompletableFuture<User> logIn(@Valid @ModelAttribute("userForm") final UserForm userForm) {
        return userService.findByLoginAndPassword(userForm.getLogin(), userForm.getPassword());
    }
}
