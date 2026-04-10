package com.learning.platform.repository;

import com.learning.platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions q WHERE q.category = :category AND q.difficulty = :difficulty AND q.id NOT IN :excludedIds ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Question findRandomQuestionByCategoryAndDifficultyExcludingIds(
            @Param("category") String category, 
            @Param("difficulty") String difficulty, 
            @Param("excludedIds") List<Long> excludedIds);

    @Query(value = "SELECT * FROM questions q WHERE q.category = :category AND q.difficulty = :difficulty ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Question findRandomQuestionByCategoryAndDifficulty(
            @Param("category") String category, 
            @Param("difficulty") String difficulty);
}
