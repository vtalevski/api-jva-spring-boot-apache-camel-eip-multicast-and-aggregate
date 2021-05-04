package com.talevski.viktor.route.enrichment;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnrichmentResultTest {

    @Test
    public void shouldEnrichmentResultContainScientistPersonalLife() {
        EnrichmentResult oldEnrichmentResult = new EnrichmentResult();
        EnrichmentResult newEnrichmentResult = new EnrichmentResult(new ScientistPersonalLifeResponse());

        oldEnrichmentResult.mergeWith(newEnrichmentResult);

        assertNotNull(oldEnrichmentResult.getScientistPersonalLifeResponse());
        assertNull(oldEnrichmentResult.getScientistProfessionalLifeResponse());
        assertEquals(emptyList(), oldEnrichmentResult.getScientistErrors());
    }

    @Test
    public void shouldEnrichmentResultContainScientistProfessionalLife() {
        EnrichmentResult oldEnrichmentResult = new EnrichmentResult();
        EnrichmentResult newEnrichmentResult = new EnrichmentResult(new ScientistProfessionalLifeResponse());

        oldEnrichmentResult.mergeWith(newEnrichmentResult);

        assertNull(oldEnrichmentResult.getScientistPersonalLifeResponse());
        assertNotNull(oldEnrichmentResult.getScientistProfessionalLifeResponse());
        assertEquals(emptyList(), oldEnrichmentResult.getScientistErrors());
    }

    @Test
    public void shouldEnrichmentResultContainScientistError() {
        EnrichmentResult oldEnrichmentResult = new EnrichmentResult();
        EnrichmentResult newEnrichmentResult = new EnrichmentResult(new ScientistError());

        oldEnrichmentResult.mergeWith(newEnrichmentResult);

        assertNull(oldEnrichmentResult.getScientistPersonalLifeResponse());
        assertNull(oldEnrichmentResult.getScientistProfessionalLifeResponse());
        assertEquals(singletonList(new ScientistError()), oldEnrichmentResult.getScientistErrors());
    }
}