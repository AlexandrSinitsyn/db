package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.SystemOnly;
import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
                         final HttpServletRequest request) {
        if (!userService.isLoginVacant(userForm.getLogin())) {
            bindingResult.rejectValue("login", "is-already-in-use");
        }

        if (bindingResult.hasErrors()) {
            return null;
        }

        final User user = userService.save(userForm.toUser(name));
        userService.updatePasswordSha(user, userForm.getPassword());

        setUserToSession(request, user);

        return user;
    }

    @RequestMapping(value = "/connect", method = {RequestMethod.GET, RequestMethod.POST})
    public String connect(final HttpServletRequest request) {
        final User user;
        if (request.getParameter("visitor") != null) {
            user = new User();
            user.setId(Tools.VISITOR_ID);
        } else {
            user = userService.findByLoginAndPassword(request.getParameter("login"), request.getParameter("password"));
        }

        setUserToSession(request, user);

        return Tools.SUCCESS_RESPONSE;
    }

    private void setUserToSession(final HttpServletRequest request, final User user) {
        request.getSession().setAttribute(Tools.USER_ID_KEY, user.getId());
    }
}
