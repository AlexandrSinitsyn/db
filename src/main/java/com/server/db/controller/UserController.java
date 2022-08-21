package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.Admin;
import com.server.db.annotations.SystemOnly;
import com.server.db.domain.User;
import com.server.db.form.UserForm;
import com.server.db.form.validator.UserFormValidator;
import com.server.db.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<List<User>> findAll() {
        return userService.findAll();
    }

    @Admin
    @PutMapping("/user/{id}/makeAdmin")
    public String makeAdmin(@PathVariable final long id) {
        userService.makeAdmin(userService.findById(id));

        return Tools.SUCCESS_RESPONSE;
    }

    @Admin
    @PutMapping("/user/{id}/downgrade")
    public String downgrade(@PathVariable final long id) {
        userService.downgrade(userService.findById(id));

        return Tools.SUCCESS_RESPONSE;
    }

    @PostMapping("user/newLogin")
    public CompletableFuture<String> updateLogin(@Valid @ModelAttribute("userForm") final UserForm userForm,
                              @RequestParam final String newLogin,
                              final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture(Tools.errorsToResponse(bindingResult));
        }

        return userService.updateLogin(userForm.toUser(userService), userForm.getPassword(), newLogin);
    }

    @PostMapping("user/newName")
    public CompletableFuture<String> updateName(@Valid @ModelAttribute("userForm") final UserForm userForm,
                             @RequestParam final String newName,
                             final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture(Tools.errorsToResponse(bindingResult));
        }

        //http://localhost:8090/api/1/connect?login=test&password=test
        //http://localhost:8090/api/1/user/newName?login=test&password=test&newName=newTest
        //http://localhost:8090/api/1/user/newName/confirm?login=test&password=test&newName=newTest

        return userService.updateName(userForm.toUser(userService), userForm.getPassword(), newName);
    }

    @PutMapping("/user/newLogin/confirm")
    @Operation(summary = "request should be sent in order to confirm 'updateLogin'",
            description = "send the same form (as for 'updateLogin'). It meant to ask user for confirmation his action")
    public CompletableFuture<String> ulConfirm(@Valid @ModelAttribute("userForm") final UserForm userForm,
                            @RequestParam final String newLogin,
                            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture(Tools.errorsToResponse(bindingResult));
        }

        final User user = userForm.toUser(userService);
        user.setAttached(userForm.getPassword());
        return userService.updateLogin(user, userForm.getPassword(), newLogin);
    }

    @PutMapping("/user/newName/confirm")
    @Operation(summary = "request should be sent in order to confirm 'updateName'",
            description = "send the same form (as for 'updateName'). It meant to ask user for confirmation his action")
    public CompletableFuture<String> unConfirm(@Valid @ModelAttribute("userForm") final UserForm userForm,
                            @RequestParam final String newName,
                            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture(Tools.errorsToResponse(bindingResult));
        }

        final User user = userForm.toUser(userService);
        user.setAttached(userForm.getPassword());
        return userService.updateName(user, userForm.getPassword(), newName);
    }

    @GetMapping("/user/find")
    public CompletableFuture<User> findAllByLogin(final String login) {
        return userService.findByLogin(login);
    }

    @GetMapping("/user/count")
    public CompletableFuture<Long> countAll() {
        return userService.countAll();
    }

    @SystemOnly
    @DeleteMapping("/user/{id}/delete")
    @Operation(summary = "this request would expect for confirmation, so it should be resent after confirmation")
    public CompletableFuture<String> deleteById(@PathVariable final long id) {
        return userService.deleteById(userService.findById(id));
    }
}
