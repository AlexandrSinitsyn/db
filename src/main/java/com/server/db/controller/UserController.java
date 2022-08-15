package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.Admin;
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

    @GetMapping("/user/all")
    public List<User> findAll() {
        return userService.findAll();
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

    @PostMapping("user/newLogin")
    public String updateLogin(@Valid @ModelAttribute("userForm") final UserForm userForm,
                              @RequestParam("newLogin") final String newLogin,
                              final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        return userService.updateLogin(userForm.toUser(userService), userForm.getPassword(), newLogin);
    }

    @PostMapping("user/newName")
    public String updateName(@Valid @ModelAttribute("userForm") final UserForm userForm,
                             @RequestParam("newName") final String newName,
                             final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        //http://localhost:8090/api/1/connect?login=test&password=test
        //http://localhost:8090/api/1/user/newName?login=test&password=test&newName=newTest
        //http://localhost:8090/api/1/user/newName/confirm?login=test&password=test&newName=newTest

        return userService.updateName(userForm.toUser(userService), userForm.getPassword(), newName);
    }

    @GetMapping("/user/newLogin/confirm")
    public String ulConfirm(@Valid @ModelAttribute("userForm") final UserForm userForm,
                          @RequestParam("newLogin") final String newLogin,
                          final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        final User user = userForm.toUser(userService);
        user.setAttached(userForm.getPassword());
        return userService.updateLogin(user, userForm.getPassword(), newLogin);
    }

    @GetMapping("/user/newName/confirm")
    public String unConfirm(@Valid @ModelAttribute("userForm") final UserForm userForm,
                            @RequestParam("newName") final String newName,
                            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        final User user = userForm.toUser(userService);
        user.setAttached(userForm.getPassword());
        return userService.updateName(user, userForm.getPassword(), newName);
    }

    @GetMapping("/user/find")
    public User findAllByLogin(final String login) {
        return userService.findByLogin(login);
    }

    @GetMapping("/user/count")
    public long countAll() {
        return userService.countAll();
    }

    @SystemOnly
    @GetMapping("/user/{id}/delete")
    public String deleteById(@PathVariable final long id) {
        return userService.deleteById(userService.findById(id));
    }
}
