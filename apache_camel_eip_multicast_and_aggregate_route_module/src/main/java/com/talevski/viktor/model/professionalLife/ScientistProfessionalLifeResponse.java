package com.talevski.viktor.model.professionalLife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistProfessionalLifeResponse {
    private ScientistProfessionalLifeResult scientistProfessionalLifeResult;
    private ScientistProfessionalLifeError scientistProfessionalLifeError;
}
