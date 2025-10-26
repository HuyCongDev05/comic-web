package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.NOSQL.Message;
import com.example.projectrestfulapi.dto.request.message.MessageRequestDTO;

import java.time.Instant;

public class MessageMapper {
    public static Message MessagePrivateMapper(MessageRequestDTO.MessagePrivate messageRequestDTO) {
        if (messageRequestDTO == null) return null;
        Message message = new Message();
        message.setChatType("private");
        message.setAccountUuidSend(messageRequestDTO.getAccountUuidSend());
        message.setFullNameAccountSend(messageRequestDTO.getFullNameAccountSend());
        message.setAvatarAccountSend(messageRequestDTO.getAvatarAccountSend());
        message.setAccountUuidReceive(messageRequestDTO.getAccountUuidReceive());
        message.setMessage(messageRequestDTO.getMessage());
        message.setTime(Instant.now());
        return message;
    }

    public static Message MessageGroupMapper(String groupId, MessageRequestDTO.MessageGroup messageRequestDTO) {
        if (messageRequestDTO == null || groupId == null) return null;
        Message message = new Message();
        message.setChatType("group");
        message.setGroupId(groupId);
        message.setAccountUuidSend(messageRequestDTO.getAccountUuidSend());
        message.setFullNameAccountSend(messageRequestDTO.getFullNameAccountSend());
        message.setAvatarAccountSend(messageRequestDTO.getAvatarAccountSend());
        message.setMessage(messageRequestDTO.getMessage());
        message.setTime(Instant.now());
        return message;
    }
}
