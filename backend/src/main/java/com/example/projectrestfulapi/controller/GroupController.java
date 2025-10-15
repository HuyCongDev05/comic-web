package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import com.example.projectrestfulapi.dto.request.message.GroupRequestDTO;
import com.example.projectrestfulapi.dto.response.message.GroupResponseDTO;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.mapper.GroupMapper;
import com.example.projectrestfulapi.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/v1")
public class GroupController {
    private final GroupService groupService;
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupResponseDTO.createGroup> createGroup(@RequestBody GroupRequestDTO.createGroupRequestDTO createGroupRequestDTO) {
        Group handleCreateGroup = groupService.handleCreateGroup(createGroupRequestDTO);
        GroupResponseDTO.createGroup createGroup = GroupMapper.mapCreateGroupResponseDTO(handleCreateGroup);
        return ResponseEntity.status(NumberError.CREATED.getStatus()).body(createGroup);
    }

    @PostMapping("/groups/join")
    public ResponseEntity<?> joinGroup(@RequestBody GroupRequestDTO groupRequestDTO) {
        return null;
    }
}
