package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.repository.SQL.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    private final StatusRepository statusRepository;
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public String handleGetStatusByUuidAccount(String accountUuid) {
        return statusRepository.findStatusByUuidAccount(accountUuid);
    }
}
