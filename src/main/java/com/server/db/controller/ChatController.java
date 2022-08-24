package com.server.db.controller;

import com.server.db.annotations.*;
import com.server.db.domain.Chat;
import com.server.db.domain.Message;
import com.server.db.domain.User;
import com.server.db.exceptions.ValidationException;
import com.server.db.form.ChatForm;
import com.server.db.form.validator.ChatFormValidator;
import com.server.db.service.ChatService;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final ChatFormValidator chatFormValidator;

    @InitBinder("chatForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(chatFormValidator);
    }

    @NoOuterAccess
    @GetMapping("/chat/all")
    public CompletableFuture<List<Chat>> findAll() {
        return chatService.findAll();
    }

    @SuppressWarnings("ConstantConditions")
    @Logined
    @GetMapping("/user/chats")
    public List<Chat> findMyChats() {
        return AccessInterceptor.sUser.getUser().getChats();
    }

    @ChatMember
    @GetMapping("/chat/{id}/users")
    public List<User> getChatUsers(@PathVariable final long id) {
        return chatService.findById(id).getUsers();
    }

    @ChatMember
    @GetMapping("/chat/{id}/messages")
    public List<Message> getAllFromChat(@PathVariable final long id,
                                        @RequestParam final int count) {
        final List<Message> res = chatService.findById(id).getMessages();
        return res.subList(0, Math.min(res.size(), count));
    }

    @ChatMember
    @GetMapping("/chat/{id}/newMessages")
    public List<Message> getNewMessages(@PathVariable final long id,
                                        @RequestParam final int count) {
        return getAllFromChat(id, count).stream().filter(Message::isUnread).toList();
    }

    @SuppressWarnings("ConstantConditions")
    @Logined
    @PostMapping("/chat/create")
    public CompletableFuture<Chat> createChat(@Valid @RequestBody final ChatForm chatForm,
                                              final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final User admin = AccessInterceptor.sUser.getUser();

        final List<User> users = Stream.concat(Stream.of(admin),
                Arrays.stream(chatForm.getMemberName()).map(userService::findByLogin)).toList();

        final var chat = new Chat();
        chat.setUsers(users);
        chat.setAdmin(admin.getLogin());

        return chatService.save(chat);
    }

    @ChatMember
    @PutMapping("/chat/{id}/addUser")
    public void addUserToChat(@PathVariable final long id,
                              @RequestParam final String login) {
        final Chat chat = chatService.findById(id);
        final User user = userService.findByLogin(login);

        if (chat.getUsers().contains(user)) {
            return;
        }

        chat.addUser(user);
        chatService.save(chat);
    }

    @ChatMember
    @PutMapping("/chat/{id}/removeUser")
    public void removeUserFromChat(@PathVariable final long id,
                                   @RequestParam final String login) {
        final Chat chat = chatService.findById(id);
        final User user = userService.findByLogin(login);

        if (!chat.getUsers().contains(user)) {
            return;
        }

        chat.removeUser(userService.findByLogin(login));
        chatService.save(chat);
    }

    @ChatMember
    @DeleteMapping("/chat/{id}/delete")
    public void deleteChat(@PathVariable final long id) {
        final Chat chat = chatService.findById(id);

        chatService.deleteById(chat);
    }
}
