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

    @PostMapping("/messages")
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return ResponseEntity.ok().body(messageService.handleSaveMessage(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(@RequestParam(name = "user_uuid") String userUuid) {
        return ResponseEntity.ok().body(messageService.handleGetAllMessagesByUserSend(userUuid));
    }
}
