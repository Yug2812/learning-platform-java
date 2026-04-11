package com.learning.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private Long id;
    private String name;
    private double cutoffCET;
    private double cutoffJEE;
    private String requiredStream;
}
