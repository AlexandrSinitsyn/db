package com.server.db.service;

import com.server.db.annotations.PrivateOnly;
import com.server.db.domain.Message;
import com.server.db.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public CompletableFuture<List<Message>> findAll() {
        return CompletableFuture.completedFuture(messageRepository.findAllByOrderByCreationTimeDesc());
    }

    public Message findById(final long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public CompletableFuture<Message> save(final Message message) {
        return CompletableFuture.completedFuture(messageRepository.save(message));
    }
}
