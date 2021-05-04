package com.talevski.viktor.model.professionalLife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistProfessionalLifeResult {
    private List<Education> educationList;
    private List<String> knownFor;
}
