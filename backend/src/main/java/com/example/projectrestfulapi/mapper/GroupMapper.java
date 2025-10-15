package com.example.projectrestfulapi.mapper;


import com.example.projectrestfulapi.domain.NOSQL.Group;
import com.example.projectrestfulapi.dto.response.message.GroupResponseDTO;

public class GroupMapper {
    public static GroupResponseDTO.createGroup mapCreateGroupResponseDTO(Group group) {
        if (group == null) return null;
        GroupResponseDTO.createGroup createGroupResponseDTO = new GroupResponseDTO.createGroup();
        createGroupResponseDTO.setGroupName(group.getGroupName());
        createGroupResponseDTO.setAvatar_url(group.getAvatar());
        createGroupResponseDTO.setCreated(group.getCreated());
        createGroupResponseDTO.setUsers(group.getUsers());
        return createGroupResponseDTO;
    }
}
