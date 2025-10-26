package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import com.example.projectrestfulapi.dto.request.message.MessageRequestDTO;
import com.example.projectrestfulapi.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> savePrivateMessage(@Valid @RequestBody MessageRequestDTO.MessagePrivate messageRequestDTO) {
        return ResponseEntity.ok().body(messageService.handleSavePrivateMessage(messageRequestDTO));
    }

    @PostMapping("/messages/group")
    public ResponseEntity<Message> saveGroupMessage(@Valid @RequestParam(name = "group_id")String groupId,@RequestBody MessageRequestDTO.MessageGroup messageRequestDTO) {
        return  ResponseEntity.ok().body(messageService.handleSaveGroupMessage(groupId, messageRequestDTO));
    }
}
