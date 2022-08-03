package com.server.db.controller;

import com.server.db.domain.Message;
import com.server.db.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/1")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("messages")
    public List<Message> findAll() {
        return messageService.findAll();
    }
}
