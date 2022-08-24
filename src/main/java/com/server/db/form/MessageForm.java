package com.server.db.form;

import com.server.db.annotations.AccessInterceptor;
import com.server.db.domain.Message;
import com.server.db.service.ChatService;
import com.server.db.service.MessageService;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MessageForm {
    private String text;

    @NotNull
    private long chatId;

    private List<Integer> links;

    public Message toMessage(final MessageService messageService, final ChatService chatService) {
        final Message message = new Message();

        if (text != null && !text.isBlank()) {
            message.setText(text);
        }

        message.setAuthor(AccessInterceptor.sUser.getUser());
        message.setChat(chatService.findById(chatId));

        if (links != null && !links.isEmpty()) {
            message.setLinks(links.stream().map(messageService::findById).toList());
        }

        return message;
    }
}
