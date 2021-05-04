package com.talevski.viktor.model.professionalLife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Education {
    private String universityName;
    private Integer graduationYear;
}
