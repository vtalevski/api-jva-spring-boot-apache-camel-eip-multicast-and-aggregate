package com.talevski.viktor.route.enrichment;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnrichmentResult {
    private ScientistPersonalLifeResponse scientistPersonalLifeResponse;
    private ScientistProfessionalLifeResponse scientistProfessionalLifeResponse;
    private List<ScientistError> scientistErrors = new ArrayList<>();

    public EnrichmentResult(ScientistPersonalLifeResponse scientistPersonalLifeResponse) {
        this.scientistPersonalLifeResponse = scientistPersonalLifeResponse;
    }

    public EnrichmentResult(ScientistProfessionalLifeResponse scientistProfessionalLifeResponse) {
        this.scientistProfessionalLifeResponse = scientistProfessionalLifeResponse;
    }

    public EnrichmentResult(ScientistError scientistError) {
        scientistErrors.add(scientistError);
    }

    public void mergeWith(EnrichmentResult newEnrichmentResult) {
        scientistPersonalLifeResponse = Optional.ofNullable(newEnrichmentResult.getScientistPersonalLifeResponse())
                .orElseGet(() -> scientistPersonalLifeResponse);
        scientistProfessionalLifeResponse = Optional.ofNullable(newEnrichmentResult.getScientistProfessionalLifeResponse())
                .orElseGet(() -> scientistProfessionalLifeResponse);
        scientistErrors.addAll(Optional.ofNullable(newEnrichmentResult.getScientistErrors()).orElseGet(() -> scientistErrors));
    }
}
