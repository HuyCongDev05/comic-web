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
    public ResponseEntity<List<Message>> getMessages(@RequestParam(name = "account_uuid") String accountUuid) {
        return ResponseEntity.ok().body(messageService.handleGetAllMessagesByUserSend(accountUuid));
    }
    @GetMapping("/comments")
    public ResponseEntity<List<Message>> getComments() {
        return ResponseEntity.ok().body(messageService.handleGetAllComments());
    }
}
