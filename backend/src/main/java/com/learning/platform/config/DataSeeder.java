package com.learning.platform.config;

import com.learning.platform.model.Category;
import com.learning.platform.model.Difficulty;
import com.learning.platform.model.Question;
import com.learning.platform.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (questionRepository.count() == 0) {
            seedQuestions();
        }
    }

    @SuppressWarnings("null")
    private void seedQuestions() {
        Question[] questions = {
                // MATH
                new Question(null, "What is the derivative of x^2?", "x", "2x", "x^3", "2", "B", Difficulty.EASY, Category.MATH),
                new Question(null, "Evaluate integral of 1/x dx.", "ln(x)", "e^x", "x^2", "1", "A", Difficulty.MEDIUM, Category.MATH),
                new Question(null, "Solve: d^2y/dx^2 + y = 0", "y=sin(x)+cos(x)", "y=e^x", "y=x^2", "y=ln(x)", "A", Difficulty.HARD, Category.MATH),
                new Question(null, "What is 5! (factorial of 5)?", "20", "60", "120", "24", "C", Difficulty.EASY, Category.MATH),
                new Question(null, "Find the determinant of a standard 2x2 identity matrix.", "0", "1", "2", "-1", "B", Difficulty.MEDIUM, Category.MATH),

                // PHYSICS
                new Question(null, "What is the SI unit of Force?", "Joule", "Newton", "Pascal", "Watt", "B", Difficulty.EASY, Category.PHYSICS),
                new Question(null, "What is the formula for kinetic energy?", "mgh", "mc^2", "1/2 mv^2", "ma", "C", Difficulty.MEDIUM, Category.PHYSICS),
                new Question(null, "According to Heisenberg's principle, you cannot simultaneously know a particle's exact location and its...?", "Mass", "Charge", "Momentum", "Spin", "C", Difficulty.HARD, Category.PHYSICS),
                new Question(null, "Light travels fastest in which medium?", "Water", "Glass", "Vacuum", "Diamond", "C", Difficulty.EASY, Category.PHYSICS),
                new Question(null, "What is the acceleration due to gravity on Earth?", "9.8 m/s^2", "10.5 m/s^2", "8.9 m/s^2", "1.2 m/s^2", "A", Difficulty.MEDIUM, Category.PHYSICS),

                // LOGIC
                new Question(null, "If all A are B, and all B are C, then:", "All C are A", "All A are C", "Some A are not C", "No A are C", "B", Difficulty.MEDIUM, Category.LOGIC),
                new Question(null, "What comes next in the sequence: 2, 4, 8, 16, ...", "20", "24", "32", "64", "C", Difficulty.EASY, Category.LOGIC),
                new Question(null, "If you rearrange the letters 'CIFAICP' you have the name of a(n):", "City", "Animal", "Ocean", "River", "C", Difficulty.HARD, Category.LOGIC), // PACIFIC
                new Question(null, "A father and son have a sum of ages of 50. The father is 20 years older than the son. How old is the son?", "10", "15", "20", "30", "B", Difficulty.MEDIUM, Category.LOGIC),

                // INTEREST
                new Question(null, "Do you enjoy assembling computers and hardware?", "Yes very much", "Sometimes", "Not really", "I dislike it", "A", Difficulty.EASY, Category.INTEREST),
                new Question(null, "Are you fascinated by how artificial intelligence models are trained?", "Yes, it's my passion", "It's okay", "I prefer hardware", "Not interested", "A", Difficulty.MEDIUM, Category.INTEREST),
                new Question(null, "Would you prefer designing a bridge over writing code?", "Yes", "Maybe", "No", "I hate both", "A", Difficulty.HARD, Category.INTEREST)
        };

        questionRepository.saveAll(Arrays.asList(questions));
        System.out.println("--- Bootstrapped 17 Sample Questions ---");
    }
}
