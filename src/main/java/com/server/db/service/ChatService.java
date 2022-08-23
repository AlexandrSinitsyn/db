package com.server.db.service;

import com.server.db.domain.Chat;
import com.server.db.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    @Async
    public CompletableFuture<List<Chat>> findAll() {
        return CompletableFuture.completedFuture(chatRepository.findAllByOrderByCreationTimeDesc());
    }

    public Chat findById(final long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Async
    public CompletableFuture<Chat> save(final Chat chat) {
        return CompletableFuture.completedFuture(chatRepository.save(chat));
    }

    @Async
    public void deleteById(final Chat chat) {
        chatRepository.deleteById(chat.getId());
    }
}
