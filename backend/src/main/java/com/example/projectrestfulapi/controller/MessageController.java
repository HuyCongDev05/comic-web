package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import com.example.projectrestfulapi.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/save/message")
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return ResponseEntity.ok().body(messageService.handleSaveMessage(message));
    }

    @GetMapping("get/list/message")
    public ResponseEntity<List<Message>> getAllMessages(String userSend) {
        return ResponseEntity.ok().body(messageService.handleGetAllMessagesByUserSend(userSend));
    }
}
