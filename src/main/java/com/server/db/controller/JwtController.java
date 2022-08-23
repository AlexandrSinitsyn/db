package com.server.db.controller;

import com.server.db.exceptions.ValidationException;
import com.server.db.form.UserForm;
import com.server.db.service.JwtService;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("jwts")
    public String create(@Valid @RequestBody final UserForm userForm,
                         final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        return jwtService.create(userForm.toUser(userService));
    }
}
