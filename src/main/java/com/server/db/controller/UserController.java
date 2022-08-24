package com.server.db.controller;

import com.server.db.annotations.AccessInterceptor;
import com.server.db.annotations.Admin;
import com.server.db.annotations.PrivateOnly;
import com.server.db.annotations.SystemOnly;
import com.server.db.domain.User;
import com.server.db.form.UserUpdateForm;
import com.server.db.form.validator.UserFormValidator;
import com.server.db.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFormValidator userUpdateFormValidator;

    @InitBinder("userUpdateForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(userUpdateFormValidator);
    }

    @GetMapping("/user/all")
    public CompletableFuture<List<User>> findAll() {
        return userService.findAll();
    }

    @Admin
    @PutMapping("/user/{id}/makeAdmin")
    public void makeAdmin(@PathVariable final long id) {
        userService.makeAdmin(userService.findById(id));
    }

    @Admin
    @PutMapping("/user/{id}/downgrade")
    public void downgrade(@PathVariable final long id) {
        userService.downgrade(userService.findById(id));
    }

    @PrivateOnly
    @PutMapping("user/newLogin")
    public void updateLogin(@Valid @RequestBody final UserUpdateForm userUpdateForm) {
        userService.updateLogin(Objects.requireNonNull(AccessInterceptor.sUser.getUser()),
                userUpdateForm.getPassword(), userUpdateForm.getAttachment());
    }

    @PrivateOnly
    @PutMapping("user/newName")
    public void updateName(@Valid @RequestBody final UserUpdateForm userUpdateForm) {
        userService.updateName(Objects.requireNonNull(AccessInterceptor.sUser.getUser()),
                userUpdateForm.getPassword(), userUpdateForm.getAttachment());
    }

    @GetMapping("/user/find")
    public User findAllByLogin(final String login) {
        return userService.findByLogin(login);
    }

    @GetMapping("/user/count")
    public CompletableFuture<Long> countAll() {
        return userService.countAll();
    }

    @SystemOnly
    @DeleteMapping("/user/{id}/delete")
    @Operation(summary = "this request would expect for confirmation, so it should be resent after confirmation")
    public void deleteById(@PathVariable final long id) {
        userService.deleteById(userService.findById(id));
    }
}
