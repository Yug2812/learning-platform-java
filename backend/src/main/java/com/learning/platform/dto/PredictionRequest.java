package com.learning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {
    private int math_score;
    private int physics_score;
    private int logic_score;
    private int interest_score;
}
