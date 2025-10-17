package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Evaluate;
import com.example.projectrestfulapi.dto.request.Evaluate.EvaluateRequestDTO;
import com.example.projectrestfulapi.dto.response.Evaluate.EvaluateResponseDTO;
import com.example.projectrestfulapi.mapper.EvaluateMapper;
import com.example.projectrestfulapi.service.EvaluateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1")
public class EvaluateController {
    private final EvaluateService evaluateService;

    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }

    @GetMapping("/evaluates")
    public ResponseEntity<EvaluateResponseDTO> getEvaluate(@RequestParam(name = "uuid_account") String uuidAccount, @RequestParam(name = "uuid_comic") String uuidComic) {
        Evaluate evaluate = evaluateService.handleGetRating(uuidAccount, uuidComic);
        EvaluateResponseDTO evaluateResponseDTO = EvaluateMapper.evaluateMapper(evaluate);
        return ResponseEntity.ok().body(evaluateResponseDTO);
    }

    @PostMapping("/evaluates")
    public ResponseEntity<EvaluateResponseDTO> postEvaluate(EvaluateRequestDTO.PostRating evaluateRequestDTO) {
        return ResponseEntity.ok().body(evaluateService.HandleCreateEvaluate(evaluateRequestDTO));
    }
}
