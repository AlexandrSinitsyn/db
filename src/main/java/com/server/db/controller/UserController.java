package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.Admin;
import com.server.db.annotations.PrivateOnly;
import com.server.db.annotations.SystemOnly;
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

    @GetMapping("/allUsers")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/logIn")
    public User logIn(@Valid @ModelAttribute("userForm") final UserForm userForm) {
        return userService.findByLoginAndPassword(userForm.getLogin(), userForm.getPassword());
    }

    @Admin
    @GetMapping("/user/{id}/makeAdmin")
    public String makeAdmin(@PathVariable final long id) {
        userService.makeAdmin(userService.findById(id));

        return Tools.SUCCESS_RESPONSE;
    }

    @Admin
    @GetMapping("/user/{id}/downgrade")
    public String downgrade(@PathVariable final long id) {
        userService.downgrade(userService.findById(id));

        return Tools.SUCCESS_RESPONSE;
    }

    @PrivateOnly
    @PostMapping("user/newLogin")
    public String updateLogin(@Valid @ModelAttribute("userForm") final UserForm userForm,
                              @RequestParam("newLogin") final String newLogin,
                              final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        return userService.updateLogin(userForm.toUser(userService), userForm.getPassword(), newLogin);
    }

    @PrivateOnly
    @PostMapping("user/newName")
    public String updateName(@Valid @ModelAttribute("userForm") final UserForm userForm,
                             @RequestParam("newName") final String newName,
                             final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        return userService.updateName(userForm.toUser(userService), userForm.getPassword(), newName);
    }

    @GetMapping("/findAll")
    public List<User> findAllByLogin(final String login) {
        return userService.findAllByLogin(login);
    }

    @GetMapping("/countUsers")
    public long countAll() {
        final var user = new User();
        user.setLogin("alexsin");
        user.setName("alexsin");
        user.setAdmin(true);
        userService.updatePasswordSha(user, "1234");
        return userService.countAll();
    }

    @SystemOnly
    @GetMapping("/deleteUser/{id}")
    public String deleteById(@PathVariable final long id) {
        return userService.deleteById(userService.findById(id));
    }
}
