package com.example.projectrestfulapi.dto.response.Evaluate;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluateResponseDTO {
    private BigDecimal rating;
    private String content;
}
