package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.SystemOnly;
import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class ConnectionController {
    private final UserService userService;

    @SystemOnly
    @PostMapping("/registerUser")
    public User register(@Valid @ModelAttribute("userForm") final UserForm userForm,
                         @RequestParam("name") final String name,
                         final BindingResult bindingResult,
                         final HttpSession session) {
        if (!userService.isLoginVacant(userForm.getLogin())) {
            bindingResult.rejectValue("login", "is-already-in-use");
        }

        if (bindingResult.hasErrors()) {
            return null;
        }

        final User user = userService.save(userForm.toUser(name));
        userService.updatePasswordSha(user, userForm.getPassword());

        session.setAttribute(Tools.USER_ID_KEY, user.getId());

        return user;
    }

    @PutMapping("/connect")
    @Operation(summary = "Provides connection to server. And sets this user 'id' to session",
            description = "Send 'visitor=true' if this user is just a visitor or 'login=...&password=...' in the opposite case")
    public String connect(@RequestParam(required = false) final String visitor,
                          @RequestParam(required = false) final String login,
                          @RequestParam(required = false) final String password,
                          final HttpSession session) {
        final User user;
        if (visitor != null) {
            user = new User();
            user.setId(Tools.VISITOR_ID);
        } else {
            user = userService.findByLoginAndPassword(login, password);
        }

        session.setAttribute(Tools.USER_ID_KEY, user.getId());

        return Tools.SUCCESS_RESPONSE;
    }

    @GetMapping("/logIn")
    @Deprecated(forRemoval = true)
    public User logIn(@Valid @ModelAttribute("userForm") final UserForm userForm) {
        return userService.findByLoginAndPassword(userForm.getLogin(), userForm.getPassword());
    }
}
