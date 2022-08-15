package com.server.db.controller;

import com.server.db.Tools;
import com.server.db.annotations.NoOuterAccess;
import com.server.db.domain.Chat;
import com.server.db.domain.Message;
import com.server.db.domain.User;
import com.server.db.form.ChatForm;
import com.server.db.form.validator.ChatFormValidator;
import com.server.db.service.ChatService;
import com.server.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    public List<Chat> findAll() {
        return chatService.findAll();
    }

    @GetMapping("/user/chats")
    public List<Chat> findMyChats(final HttpSession session) {
        return userService.findById(Tools.getUserFromSession(session, userService).getId()).getChats();
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
    public Chat createChat(@Valid @ModelAttribute("chatForm") final ChatForm chatForm,
                           final BindingResult bindingResult,
                           final HttpSession session) {
        if (bindingResult.hasErrors()) {
            return null;
        }

        final var chat = new Chat();
        chat.setUsers(Arrays.stream(chatForm.getMemberName()).map(userService::findByLogin).toList());
        chat.setAdmin(Tools.getUserFromSession(session, userService).getLogin());

        return chatService.save(chat);
    }

    @PostMapping("/chat/{chatId}/addUser")
    public String addUserToChat(@PathVariable final long chatId,
                                @RequestParam("login") final String login) {
        final Chat chat = chatService.findById(chatId);
        chat.addUser(userService.findByLogin(login));
        chatService.save(chat);

        return Tools.SUCCESS_RESPONSE;
    }

    @PostMapping("/chat/{chatId}/removeUser")
    public String removeUserFromChat(@PathVariable final long chatId,
                                     @RequestParam("login") final String login) {
        final Chat chat = chatService.findById(chatId);
        chat.removeUser(userService.findByLogin(login));
        chatService.save(chat);

        return Tools.SUCCESS_RESPONSE;
    }

    @PostMapping("/chat/{chatId}/delete")
    public String deleteChat(@PathVariable final long chatId,
                             final HttpSession session) {
        final User user = Tools.getUserFromSession(session, userService);
        final Chat chat = chatService.findById(chatId);

        if (user == null || !Objects.equals(user.getLogin(), chat.getAdmin())) {
            return "redirect:/accessDenied";
        }

        return chatService.deleteById(chat);
    }
}
