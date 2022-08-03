package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.PrivateOnly;
import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.form.validator.UserFormValidator;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFormValidator userFormValidator;

    @InitBinder("userForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(userFormValidator);
    }

    @GetMapping("allUsers")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("registerUser")
    public String register(@Valid @ModelAttribute("guestForm") final UserForm userForm,
                         final BindingResult bindingResult) {
        if (!userService.isLoginVacant(userForm.getLogin())) {
            bindingResult.rejectValue("login", "is-already-in-use");
        }

        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        final User user = userService.save(userForm.toUser(userService));

        return Tools.userToJsonString(user);
    }

    @GetMapping("logIn")
    public User logIn(final String login, final String password) {
        return login == null || password == null ? null
                : userService.findByLoginAndPassword(login, password);
    }

    @GetMapping("user/{id}/makeAdmin")
    public void makeAdmin(@PathVariable final long id) {
        userService.makeAdmin(userService.findById(id));
    }

    @GetMapping("user/{id}/downgrade")
    public void downgrade(@PathVariable final long id) {
        userService.downgrade(userService.findById(id));
    }

    @PrivateOnly
    @PostMapping("user/newLogin")
    public String updateLogin(@Valid @ModelAttribute("userForm") final UserForm userForm,
                              @RequestParam("newLogin") final String newLogin,
                            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        userService.updateLogin(userForm.toUser(userService), userForm.getPassword(), newLogin);

        return Tools.SUCCESS_RESPONSE;
    }

    @PrivateOnly
    @PostMapping("user/newName")
    public String updateName(@Valid @ModelAttribute("userForm") final UserForm userForm,
                             @RequestParam("newLogin") final String newName,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        userService.updateName(userForm.toUser(userService), userForm.getPassword(), newName);

        return Tools.SUCCESS_RESPONSE;
    }

    @GetMapping("findAll:login={login}")
    public List<User> findAllByLogin(@PathVariable final String login) {
        return userService.findAllByLogin(login);
    }

    @GetMapping("countUsers")
    public long countAll() {
        return userService.countAll();
    }

    @GetMapping("deleteUser/{id}")
    public void deleteById(@PathVariable final long id) {
        userService.deleteById(userService.findById(id));
    }
}
