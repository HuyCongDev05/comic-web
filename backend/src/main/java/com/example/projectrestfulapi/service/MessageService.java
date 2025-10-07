package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import com.example.projectrestfulapi.repository.NOSQL.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message handleSaveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> handleGetAllMessagesByUserSend(String userSend) {
        return messageRepository.findByUserSend(userSend);
    }

    public List<Message> handleGetGroupMessagesByUserSend(String userSend, String groupId) {
        return messageRepository.findAllByUserSendAndGroupId(userSend, groupId);
    }
}
