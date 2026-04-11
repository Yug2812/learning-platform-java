package com.learning.platform.controller;

import com.learning.platform.dto.QuizDto;
import com.learning.platform.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/start")
    public ResponseEntity<QuizDto.QuestionResponse> startQuiz(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(quizService.startQuiz(userEmail));
    }

    @PostMapping("/answer")
    public ResponseEntity<QuizDto.QuestionResponse> submitAnswer(
            @RequestBody QuizDto.AnswerRequest answerRequest,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        return ResponseEntity.ok(quizService.processAnswer(answerRequest, userEmail));
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizDto.SubmitResponse> finishQuiz(
            @RequestBody QuizDto.SubmitRequest submitRequest,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        return ResponseEntity.ok(quizService.submitQuiz(submitRequest, userEmail));
    }
}
