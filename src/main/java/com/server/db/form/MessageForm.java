package com.server.db.form;

import com.server.db.domain.Message;
import com.server.db.service.ChatService;
import com.server.db.service.JwtService;
import com.server.db.service.MessageService;
import com.server.db.service.UserService;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class MessageForm {
    @NotNull
    @NotBlank
    private String jwt;

    private String text;

    @NotNull
    private long chatId;

    private List<Integer> links;

    public Message toMessage(final JwtService jwtService, final MessageService messageService, final ChatService chatService) {
        final Message message = new Message();

        if (text != null && !text.isBlank()) {
            message.setText(text);
        }

        message.setAuthor(jwtService.findUser(jwt));
        message.setChat(chatService.findById(chatId));

        if (links != null && !links.isEmpty()) {
            message.setLinks(links.stream().map(messageService::findById).toList());
        }

        return message;
    }
}
