package com.learning.platform.service;

import com.learning.platform.dto.PredictionRequest;
import com.learning.platform.dto.PredictionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PredictionService {

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String predictField(int mathScore, int physicsScore, int logicScore, int interestScore) {
        String endpoint = aiServiceUrl + "/predict";
        
        PredictionRequest request = PredictionRequest.builder()
                .math_score(mathScore)
                .physics_score(physicsScore)
                .logic_score(logicScore)
                .interest_score(interestScore)
                .build();

        try {
            PredictionResponse response = restTemplate.postForObject(endpoint, request, PredictionResponse.class);
            if (response != null && response.getPredicted_field() != null) {
                return response.getPredicted_field();
            }
            return "Prediction service returned empty response";
        } catch (Exception e) {
            log.error("Error communicating with AI prediction service: {}", e.getMessage());
            return "Prediction service unavailable";
        }
    }
}
