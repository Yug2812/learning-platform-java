package com.learning.platform.service;

import com.learning.platform.dto.QuizDto;
import com.learning.platform.model.*;
import com.learning.platform.repository.QuestionRepository;
import com.learning.platform.repository.QuizAttemptRepository;
import com.learning.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class QuizService {

    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final PredictionService predictionService;
    private final EligibilityService eligibilityService;

    private static final int MAX_QUESTIONS = 10;

    @Transactional
    public QuizDto.QuestionResponse startQuiz(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        QuizAttempt attempt = QuizAttempt.builder()
                .user(user)
                .totalQuestions(MAX_QUESTIONS)
                .score(0)
                .mathScore(0)
                .physicsScore(0)
                .logicScore(0)
                .interestScore(0)
                .currentDifficulty(Difficulty.MEDIUM)
                .currentCategory(Category.MATH)
                .completed(false)
                .build();
        
        quizAttemptRepository.save(attempt);

        return getNextQuestion(attempt);
    }

    @Transactional
    public QuizDto.QuestionResponse processAnswer(QuizDto.AnswerRequest request, String userEmail) {
        QuizAttempt attempt = quizAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));

        if (attempt.isCompleted()) {
            throw new RuntimeException("Quiz already completed");
        }
        
        // Ensure user is authorized to answer this attempt
        if (!attempt.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized quiz access");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // Assess exact match
        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(request.getSelectedAnswer());

        // Update scores and adjust difficulty
        updateScoreAndDifficulty(attempt, question.getCategory(), isCorrect);
        
        // Mark question as answered
        attempt.getAnsweredQuestionIds().add(question.getId());

        // Shift Category to next
        attempt.setCurrentCategory(getNextCategory(attempt.getCurrentCategory()));

        quizAttemptRepository.save(attempt);

        // Fetch Next or Complete
        return getNextQuestion(attempt);
    }

    @Transactional
    public QuizDto.SubmitResponse submitQuiz(QuizDto.SubmitRequest request, String userEmail) {
        QuizAttempt attempt = quizAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));

        if (!attempt.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized quiz access");
        }

        attempt.setCompleted(true);
        quizAttemptRepository.save(attempt);

        // Fetch AI Prediction
        String prediction = predictionService.predictField(
                attempt.getMathScore(),
                attempt.getPhysicsScore(),
                attempt.getLogicScore(),
                attempt.getInterestScore()
        );

        Map<String, String> branchAnalysis = null;
        List<Course> recommendedCourses = null;

        if (request.getStudentProfile() != null) {
            branchAnalysis = eligibilityService.analyzeEligibility(request.getStudentProfile());
            String bestChance = branchAnalysis.getOrDefault(prediction, "Low Chances");
            recommendedCourses = eligibilityService.recommendCourses(prediction, bestChance);
        }

        return QuizDto.SubmitResponse.builder()
                .attemptId(attempt.getId())
                .totalQuestions(attempt.getTotalQuestions())
                .score(attempt.getScore())
                .mathScore(attempt.getMathScore())
                .physicsScore(attempt.getPhysicsScore())
                .logicScore(attempt.getLogicScore())
                .interestScore(attempt.getInterestScore())
                .predictedField(prediction)
                .branchAnalysis(branchAnalysis)
                .recommendedCourses(recommendedCourses)
                .build();
    }

    // --- Private Helper Methods ---

    private void updateScoreAndDifficulty(QuizAttempt attempt, Category category, boolean isCorrect) {
        if (isCorrect) {
            // Give more weight to HARD questions
            int points = attempt.getCurrentDifficulty() == Difficulty.HARD ? 3 :
                         attempt.getCurrentDifficulty() == Difficulty.MEDIUM ? 2 : 1;

            attempt.setScore(attempt.getScore() + points);

            switch (category) {
                case MATH -> attempt.setMathScore(attempt.getMathScore() + points);
                case PHYSICS -> attempt.setPhysicsScore(attempt.getPhysicsScore() + points);
                case LOGIC -> attempt.setLogicScore(attempt.getLogicScore() + points);
                case INTEREST -> attempt.setInterestScore(attempt.getInterestScore() + points);
            }

            // Adaptive: Shift Difficulty UP
            if (attempt.getCurrentDifficulty() == Difficulty.EASY) attempt.setCurrentDifficulty(Difficulty.MEDIUM);
            else if (attempt.getCurrentDifficulty() == Difficulty.MEDIUM) attempt.setCurrentDifficulty(Difficulty.HARD);
        } else {
            // Adaptive: Shift Difficulty DOWN
            if (attempt.getCurrentDifficulty() == Difficulty.HARD) attempt.setCurrentDifficulty(Difficulty.MEDIUM);
            else if (attempt.getCurrentDifficulty() == Difficulty.MEDIUM) attempt.setCurrentDifficulty(Difficulty.EASY);
        }
    }

    private Category getNextCategory(Category current) {
        return switch (current) {
            case MATH -> Category.PHYSICS;
            case PHYSICS -> Category.LOGIC;
            case LOGIC -> Category.INTEREST;
            case INTEREST -> Category.MATH;
        };
    }

    private QuizDto.QuestionResponse getNextQuestion(QuizAttempt attempt) {
        int answeredCount = attempt.getAnsweredQuestionIds().size();

        // Check if quiz is concluded
        if (answeredCount >= attempt.getTotalQuestions()) {
            attempt.setCompleted(true);
            quizAttemptRepository.save(attempt);
            return QuizDto.QuestionResponse.builder()
                    .attemptId(attempt.getId())
                    .isCompleted(true)
                    .build();
        }

        // Fetch Question
        Question nextQ = fetchNextAdaptiveQuestion(
                attempt.getCurrentCategory().name(),
                attempt.getCurrentDifficulty().name(),
                attempt.getAnsweredQuestionIds()
        );

        // Fallback: if no question matches difficulty/category, fetch any category with that difficulty
        if (nextQ == null) {
            nextQ = questionRepository.findRandomQuestionByCategoryAndDifficultyExcludingIds(
                    Category.MATH.name(), attempt.getCurrentDifficulty().name(), 
                    attempt.getAnsweredQuestionIds().isEmpty() ? List.of(-1L) : attempt.getAnsweredQuestionIds());
        }

        if (nextQ == null) {
             throw new RuntimeException("Not enough questions in database to continue.");
        }

        return QuizDto.QuestionResponse.builder()
                .attemptId(attempt.getId())
                .questionId(nextQ.getId())
                .questionText(nextQ.getQuestionText())
                .optionA(nextQ.getOptionA())
                .optionB(nextQ.getOptionB())
                .optionC(nextQ.getOptionC())
                .optionD(nextQ.getOptionD())
                .category(nextQ.getCategory())
                .difficulty(nextQ.getDifficulty())
                .currentQuestionNumber(answeredCount + 1)
                .isCompleted(false)
                .build();
    }

    private Question fetchNextAdaptiveQuestion(String category, String difficulty, List<Long> excludedIds) {
        if (excludedIds == null || excludedIds.isEmpty()) {
            return questionRepository.findRandomQuestionByCategoryAndDifficulty(category, difficulty);
        }
        return questionRepository.findRandomQuestionByCategoryAndDifficultyExcludingIds(category, difficulty, excludedIds);
    }

}
