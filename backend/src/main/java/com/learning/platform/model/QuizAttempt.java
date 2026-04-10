package com.learning.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int totalQuestions;
    
    private int score;
    private int mathScore;
    private int physicsScore;
    private int logicScore;
    private int interestScore;

    @Enumerated(EnumType.STRING)
    private Difficulty currentDifficulty;

    @Enumerated(EnumType.STRING)
    private Category currentCategory;

    private boolean completed;

    @ElementCollection
    @CollectionTable(name = "quiz_attempt_questions", joinColumns = @JoinColumn(name = "quiz_attempt_id"))
    @Column(name = "question_id")
    @Builder.Default
    private List<Long> answeredQuestionIds = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
