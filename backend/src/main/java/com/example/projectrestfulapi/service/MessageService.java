package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import com.example.projectrestfulapi.dto.request.message.MessageRequestDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.mapper.MessageMapper;
import com.example.projectrestfulapi.repository.NOSQL.GroupRepository;
import com.example.projectrestfulapi.repository.NOSQL.MessageRepository;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, GroupRepository groupRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.accountRepository = accountRepository;
    }

    public Message handleSavePrivateMessage(MessageRequestDTO.MessagePrivate messageRequestDTO) {
        if (!accountRepository.existsByUuid(messageRequestDTO.getAccountUuidSend()) || accountRepository.existsByUuid(messageRequestDTO.getAccountUuidReceive())) {
            throw new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND);
        } else if (messageRequestDTO.getAccountUuidSend().equals(messageRequestDTO.getAccountUuidReceive())) {
            throw new InvalidException(NumberError.DUPLICATE_ACCOUNT.getMessage(), NumberError.DUPLICATE_ACCOUNT);
        }
        Message message = MessageMapper.MessagePrivateMapper(messageRequestDTO);
        return messageRepository.save(message);
    }

    public Message handleSaveGroupMessage(String groupId, MessageRequestDTO.MessageGroup messageRequestDTO) {
        if (!groupRepository.existsById(groupId)) {
            throw new InvalidException(NumberError.GROUP_NOT_FOUND.getMessage(), NumberError.GROUP_NOT_FOUND);
        } else if (!accountRepository.existsByUuid(messageRequestDTO.getAccountUuidSend())) {
            throw new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND);
        }
        Message message = MessageMapper.MessageGroupMapper(groupId, messageRequestDTO);
        return messageRepository.save(message);
    }
}
