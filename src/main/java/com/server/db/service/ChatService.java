package com.server.db.service;

import com.server.db.Tools;
import com.server.db.annotations.Confirmation;
import com.server.db.domain.Chat;
import com.server.db.domain.Chat;
import com.server.db.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat findById(final long id) {
        return chatRepository.findById(id).orElse(null);
    }

    public Chat save(final Chat chat) {
        return chatRepository.save(chat);
    }

    @Confirmation("action")
    public String deleteById(final Chat chat) {
        chatRepository.deleteById(chat.getId());

        return Tools.SUCCESS_RESPONSE;
    }
}
