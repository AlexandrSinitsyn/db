package com.server.db.service;

import com.server.db.domain.Message;
import com.server.db.domain.User;
import com.server.db.repository.MessageRepository;
import com.server.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
