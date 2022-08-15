package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.NoOuterAccess;
import com.server.db.annotations.PrivateOnly;
import com.server.db.domain.Message;
import com.server.db.form.MessageForm;
import com.server.db.form.validator.MessageFormValidator;
import com.server.db.service.ChatService;
import com.server.db.service.MessageService;
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
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final MessageFormValidator messageFormValidator;

    @InitBinder("messageForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(messageFormValidator);
    }

    @NoOuterAccess
    @GetMapping("/message/all")
    public List<Message> findAll() {
        return messageService.findAll();
    }

    @PostMapping("/message/write")
    public Message writeMessage(@Valid @ModelAttribute("messageForm") final MessageForm messageForm,
                               final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        final Message message = messageForm.toMessage(userService, messageService, chatService);

        return messageService.save(message);
    }

    @PostMapping("/message/{id}/rewrite")
    public String rewriteMessage(@PathVariable final long id,
                                 @Valid @ModelAttribute("messageForm") final MessageForm messageForm,
                                 final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Tools.errorsToResponse(bindingResult);
        }

        final Message message = messageService.findById(id);
        final Message upd = messageForm.toMessage(userService, messageService, chatService);

        message.setText(upd.getText());
        message.setLinks(upd.getLinks());

        messageService.save(message);

        return Tools.SUCCESS_RESPONSE;
    }
}
