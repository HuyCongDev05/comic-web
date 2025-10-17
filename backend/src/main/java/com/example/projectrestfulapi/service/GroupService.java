package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import com.example.projectrestfulapi.dto.request.message.GroupRequestDTO;
import com.example.projectrestfulapi.repository.NOSQL.GroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group handleCreateGroup(GroupRequestDTO.createGroupRequestDTO createGroupRequestDTO) {
        Group group = new Group(createGroupRequestDTO.getGroupName(), createGroupRequestDTO.getAvatar(), createGroupRequestDTO.getUsers());
        return groupRepository.save(group);
    }
}
