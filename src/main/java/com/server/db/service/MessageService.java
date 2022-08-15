package com.server.db.service;

import com.server.db.annotations.PrivateOnly;
import com.server.db.domain.Message;
import com.server.db.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreationTimeDesc();
    }

    public Message findById(final long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @PrivateOnly
    public Message save(final Message message) {
        return messageRepository.save(message);
    }
}
