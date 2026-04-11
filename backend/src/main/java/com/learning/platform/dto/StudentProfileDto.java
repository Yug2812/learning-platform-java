package com.learning.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileDto {
    private String name;
    private double class10Score;
    private double class12Score;
    private String stream; // PCM, PCB, PCMB
    private double jeeScore;
    private double cetScore;
}
