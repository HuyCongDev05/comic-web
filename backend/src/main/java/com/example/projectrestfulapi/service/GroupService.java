package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import com.example.projectrestfulapi.dto.request.message.GroupRequestDTO;
import com.example.projectrestfulapi.repository.NOSQL.GroupRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
