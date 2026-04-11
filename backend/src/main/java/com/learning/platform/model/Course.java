package com.learning.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;
    private String title;
    private String platform;
    private String link;
    private String type; // FREE / PAID
    private String field;
    private String level; // BEGINNER / ADVANCED
}
