package com.server.db.controller;

import com.server.db.domain.Chat;
import com.server.db.domain.Message;
import com.server.db.domain.User;
import com.server.db.exceptions.ValidationException;
import com.server.db.form.ChatForm;
import com.server.db.form.validator.ChatFormValidator;
import com.server.db.service.ChatService;
import com.server.db.service.JwtService;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ChatFormValidator chatFormValidator;

    @InitBinder("chatForm")
    public void initBinder(final WebDataBinder binder) {
        binder.addValidators(chatFormValidator);
    }

    @GetMapping("/chat/all")
    public CompletableFuture<List<Chat>> findAll() {
        return chatService.findAll();
    }

    @GetMapping("/user/chats")
    public List<Chat> findMyChats(@RequestParam final String jwt) {
        return jwtService.findUser(jwt).getChats();
    }

    @GetMapping("/chat/{id}/users")
    public List<User> getChatUsers(@PathVariable final long id) {
        final Chat chat = chatService.findById(id);
        return Stream.concat(Stream.of(userService.findByLogin(chat.getAdmin())),
                chat.getUsers().stream()).toList();
    }

    @GetMapping("/chat/{id}/messages")
    public List<Message> getAllFromChat(@PathVariable final long id,
                                        @RequestParam final int count) {
        final List<Message> res = chatService.findById(id).getMessages().stream().sorted(Comparator.comparing(Message::getCreationTime)).toList();
        return res.subList(0, Math.min(res.size(), count));
    }

    @GetMapping("/chat/{id}/newMessages")
    public List<Message> getNewMessages(@PathVariable final long id,
                                        @RequestParam final int count) {
        return getAllFromChat(id, count).stream().filter(Message::isUnread).toList();
    }

    @PostMapping("/chat/create")
    public CompletableFuture<Chat> createChat(@Valid @RequestBody final ChatForm chatForm,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        final User admin = jwtService.findUser(chatForm.getJwt());

        final List<User> users = Stream.concat(Stream.of(admin),
                Arrays.stream(chatForm.getMemberName()).map(userService::findByLogin)).toList();

        final var chat = new Chat();
        chat.setUsers(users);
        chat.setAdmin(admin.getLogin());

        return chatService.save(chat);
    }

    @PutMapping("/chat/{id}/addUser")
    public void addUserToChat(@PathVariable final long id,
                                @RequestParam final String login) {
        final Chat chat = chatService.findById(id);
        chat.addUser(userService.findByLogin(login));
        chatService.save(chat);
    }

    @PutMapping("/chat/{id}/removeUser")
    public void removeUserFromChat(@PathVariable final long id,
                                     @RequestParam final String login) {
        final Chat chat = chatService.findById(id);
        chat.removeUser(userService.findByLogin(login));
        chatService.save(chat);
    }

    @DeleteMapping("/chat/{chatId}/delete")
    public void deleteChat(@PathVariable final long chatId,
                           @RequestParam final String jwt) {
        final User user = jwtService.findUser(jwt);
        final Chat chat = chatService.findById(chatId);

        if (user == null || !Objects.equals(user.getLogin(), chat.getAdmin())) {
            return;
        }

        chatService.deleteById(chat);
    }
}
