package com.talevski.viktor.route.processor;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.model.ScientistResult;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResult;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResult;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME;

@Component
public class MapperProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        EnrichmentResult enrichmentResult = exchange.getIn().getBody(EnrichmentResult.class);

        ScientistPersonalLifeResponse scientistPersonalLifeResponse = enrichmentResult.getScientistPersonalLifeResponse();
        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = enrichmentResult.getScientistProfessionalLifeResponse();
        List<ScientistError> scientistErrors = enrichmentResult.getScientistErrors();

        ScientistPersonalLifeResult scientistPersonalLifeResult = null;
        ScientistProfessionalLifeResult scientistProfessionalLifeResult = null;
        if (scientistPersonalLifeResponse != null)
            scientistPersonalLifeResult = scientistPersonalLifeResponse.getScientistPersonalLifeResult();
        if (scientistProfessionalLifeResponse != null)
            scientistProfessionalLifeResult = scientistProfessionalLifeResponse.getScientistProfessionalLifeResult();

        ScientistResponse scientistResponse = new ScientistResponse();
        scientistResponse.setScientistResult(getScientistResult(exchange, scientistPersonalLifeResult, scientistProfessionalLifeResult));
        if (!scientistErrors.isEmpty()) scientistResponse.setScientistErrors(scientistErrors);

        exchange.getIn().setBody(scientistResponse);
    }

    private ScientistResult getScientistResult(Exchange exchange,
                                               ScientistPersonalLifeResult scientistPersonalLifeResult,
                                               ScientistProfessionalLifeResult scientistProfessionalLifeResult) {
        ScientistResult result = new ScientistResult();
        result.setFirstName(exchange.getProperty(FIRST_NAME, String.class));
        result.setLastName(exchange.getProperty(LAST_NAME, String.class));

        Optional.ofNullable(scientistPersonalLifeResult).map(notNullScientistPersonalLifeResult -> {
            result.setBirthYear(notNullScientistPersonalLifeResult.getBirthYear());
            result.setDeathYear(notNullScientistPersonalLifeResult.getDeathYear());
            result.setBirthCountry(notNullScientistPersonalLifeResult.getBirthCountry());
            result.setDeathCountry(notNullScientistPersonalLifeResult.getDeathCountry());
            result.setWives(notNullScientistPersonalLifeResult.getWives());
            return null;
        }).orElse(null);
        Optional.ofNullable(scientistProfessionalLifeResult).map(notNullScientistProfessionalLifeResult -> {
            result.setEducationList(notNullScientistProfessionalLifeResult.getEducationList());
            result.setKnownFor(notNullScientistProfessionalLifeResult.getKnownFor());
            return null;
        }).orElse(null);

        return result;
    }
}
