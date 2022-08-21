package com.server.db.form;

import com.server.db.domain.Message;
import com.server.db.service.ChatService;
import com.server.db.service.MessageService;
import com.server.db.service.UserService;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MessageForm {
    private String text;

    @NotNull
    private long chatId;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String author;

    private List<Integer> links;

    public Message toMessage(final UserService userService, final MessageService messageService, final ChatService chatService) {
        final Message message = new Message();

        if (text != null && !text.isBlank()) {
            message.setText(text);
        }

        message.setAuthor(userService.findByLogin(author).join());
        message.setChat(chatService.findById(chatId));

        if (links != null && !links.isEmpty()) {
            message.setLinks(links.stream().map(messageService::findById).toList());
        }

        return message;
    }
}
