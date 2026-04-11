package com.learning.platform.controller;

import com.learning.platform.dto.StudentProfileDto;
import com.learning.platform.service.EligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeEligibility(@RequestBody StudentProfileDto profile) {
        Map<String, String> branchAnalysis = eligibilityService.analyzeEligibility(profile);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("branchAnalysis", branchAnalysis);
        response.put("note", "This is a prediction based on past trends and not a final admission decision.");
        
        return ResponseEntity.ok(response);
    }
}
