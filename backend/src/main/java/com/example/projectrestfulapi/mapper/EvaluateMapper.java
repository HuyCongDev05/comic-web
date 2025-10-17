package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Evaluate;
import com.example.projectrestfulapi.dto.response.Evaluate.EvaluateResponseDTO;

public class EvaluateMapper {
    public static EvaluateResponseDTO evaluateMapper(Evaluate evaluate) {
        if (evaluate == null) return null;
        EvaluateResponseDTO evaluateResponseDTO = new EvaluateResponseDTO();
        evaluateResponseDTO.setRating(evaluate.getRating());
        evaluateResponseDTO.setContent(evaluate.getContent());
        return evaluateResponseDTO;
    }
}
