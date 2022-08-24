package com.server.db.controller;

import com.server.db.annotations.Logined;
import com.server.db.annotations.NoOuterAccess;
import com.server.db.annotations.PrivateOnly;
import com.server.db.domain.Message;
import com.server.db.exceptions.ValidationException;
import com.server.db.form.MessageForm;
import com.server.db.form.validator.MessageFormValidator;
import com.server.db.service.ChatService;
import com.server.db.service.MessageService;
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
public class MessageController {
    private final MessageService messageService;
    private final ChatService chatService;
    private final MessageFormValidator messageFormValidator;

    @InitBinder("messageForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(messageFormValidator);
    }

    @NoOuterAccess
    @GetMapping("/message/all")
    public CompletableFuture<List<Message>> findAll() {
        return messageService.findAll();
    }

    @Logined
    @PostMapping("/message/write")
    public CompletableFuture<Message> writeMessage(@Valid @RequestBody final MessageForm messageForm,
                                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final Message message = messageForm.toMessage(messageService, chatService);

        return messageService.save(message);
    }

    @PrivateOnly
    @PutMapping("/message/{id}/rewrite")
    public void rewriteMessage(@PathVariable final long id,
                               @Valid @RequestBody final MessageForm messageForm,
                               final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final Message message = messageService.findById(id);
        final Message upd = messageForm.toMessage(messageService, chatService);

        message.setText(upd.getText());
        message.setLinks(upd.getLinks());

        messageService.save(message);
    }
}
