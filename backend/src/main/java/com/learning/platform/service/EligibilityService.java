package com.learning.platform.service;

import com.learning.platform.dto.StudentProfileDto;
import com.learning.platform.model.Branch;
import com.learning.platform.model.Course;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EligibilityService {

    private final List<Branch> branches = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();

    public EligibilityService() {
        // Initialize dummy branches with static cutoffs for MIT-WPU
        branches.add(new Branch(1L, "Computer Engineering", 90.0, 85.0, "PCM"));
        branches.add(new Branch(2L, "Mechanical Engineering", 80.0, 70.0, "PCM"));
        branches.add(new Branch(3L, "Civil Engineering", 75.0, 65.0, "PCM"));
        branches.add(new Branch(4L, "Electronics Engineering", 85.0, 75.0, "PCM"));
        
        // Initialize dummy courses
        courses.add(new Course(1L, "Intro to Python & C++", "Coursera", "https://coursera.org", "FREE", "Computer Engineering", "BEGINNER"));
        courses.add(new Course(2L, "Advanced Machine Learning & DSA", "Udemy", "https://udemy.com", "PAID", "Computer Engineering", "ADVANCED"));
        
        courses.add(new Course(3L, "AutoCAD Basics", "YouTube", "https://youtube.com", "FREE", "Mechanical Engineering", "BEGINNER"));
        courses.add(new Course(4L, "Thermodynamics & Fluid Mechanics", "edX", "https://edx.org", "PAID", "Mechanical Engineering", "ADVANCED"));

        courses.add(new Course(5L, "Structural Analysis Fundamentals", "Coursera", "https://coursera.org", "FREE", "Civil Engineering", "BEGINNER"));
        courses.add(new Course(6L, "Advanced Urban Planning", "Udemy", "https://udemy.com", "PAID", "Civil Engineering", "ADVANCED"));

        courses.add(new Course(7L, "Basic Circuit Design", "YouTube", "https://youtube.com", "FREE", "Electronics Engineering", "BEGINNER"));
        courses.add(new Course(8L, "VLSI System Design", "edX", "https://edx.org", "PAID", "Electronics Engineering", "ADVANCED"));
        
        // Add some general fallback courses
        courses.add(new Course(9L, "First Year Engineering Math", "Coursera", "https://coursera.org", "FREE", "General", "BEGINNER"));
        courses.add(new Course(10L, "Advanced Physics Concepts", "Udemy", "https://udemy.com", "PAID", "General", "ADVANCED"));
    }

    public Map<String, String> analyzeEligibility(StudentProfileDto profile) {
        Map<String, String> analysis = new LinkedHashMap<>();

        // We will prioritize CET if both are provided, otherwise use JEE.
        // Let's analyze both and give the best chance found.
        
        for (Branch branch : branches) {
            // Very simple filtering
            if (profile.getStream() != null && !profile.getStream().contains("PCM") && branch.getRequiredStream().equals("PCM")) {
                analysis.put(branch.getName(), "Low Chances (Stream Requirement Not Met)");
                continue;
            }

            double studentScoreCet = profile.getCetScore();
            double studentScoreJee = profile.getJeeScore();
            
            String cetChance = calculateChance(studentScoreCet, branch.getCutoffCET());
            String jeeChance = calculateChance(studentScoreJee, branch.getCutoffJEE());
            
            // Assign best chance among CET and JEE
            String bestChance = mapBestChance(cetChance, jeeChance);
            analysis.put(branch.getName(), bestChance);
        }
        
        return analysis;
    }

    private String calculateChance(double score, double cutoff) {
        if (score == 0) return "Not Attempted";
        if (score >= cutoff + 5) {
            return "High Chances";
        } else if (score >= cutoff - 5) { // Score within 5 marks of cutoff
            return "Moderate Chances";
        } else {
            return "Low Chances";
        }
    }
    
    private String mapBestChance(String chance1, String chance2) {
        List<String> ranks = Arrays.asList("High Chances", "Moderate Chances", "Low Chances", "Not Attempted");
        int rank1 = ranks.indexOf(chance1);
        int rank2 = ranks.indexOf(chance2);
        
        if (rank1 == -1) rank1 = 3;
        if (rank2 == -1) rank2 = 3;
        
        return ranks.get(Math.min(rank1, rank2));
    }

    public List<Course> recommendCourses(String predictedField, String topBranchChance) {
        List<Course> recommended = new ArrayList<>();
        
        String levelTarget = "BEGINNER";
        if ("High Chances".equals(topBranchChance)) {
            levelTarget = "ADVANCED";
        }

        // Match courses for the predicted field and level
        for (Course course : courses) {
            if (course.getField().equalsIgnoreCase(predictedField) && course.getLevel().equalsIgnoreCase(levelTarget)) {
                recommended.add(course);
            }
        }
        
        // Include one general course just in case
        for (Course course : courses) {
            if (course.getField().equalsIgnoreCase("General") && course.getLevel().equalsIgnoreCase(levelTarget)) {
                recommended.add(course);
                break;
            }
        }

        return recommended;
    }
}
