package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.dto.request.Comic.FollowAndHistoryComicRequestDTO;
import com.example.projectrestfulapi.service.HistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/accounts/history")
    public ResponseEntity<Void> saveHistory(@Valid @RequestBody FollowAndHistoryComicRequestDTO followComicRequestDTO) {
        historyService.handleSave(followComicRequestDTO.getAccountUuid(), followComicRequestDTO.getComicUuid());
        return ResponseEntity.ok().build();
    }
}
