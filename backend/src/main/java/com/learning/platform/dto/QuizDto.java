package com.learning.platform.dto;

import com.learning.platform.model.Category;
import com.learning.platform.model.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QuizDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResponse {
        private Long attemptId;
        private Long questionId;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private Difficulty difficulty;
        private Category category;
        private int currentQuestionNumber;
        private boolean isCompleted;
    }

    @Data
    public static class AnswerRequest {
        private Long attemptId;
        private Long questionId;
        private String selectedAnswer; // "A", "B", "C", "D"
    }

    @Data
    public static class SubmitRequest {
        private Long attemptId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitResponse {
        private Long attemptId;
        private int totalQuestions;
        private int score;
        private int mathScore;
        private int physicsScore;
        private int logicScore;
        private int interestScore;
        private String recommendedField;
    }
}
