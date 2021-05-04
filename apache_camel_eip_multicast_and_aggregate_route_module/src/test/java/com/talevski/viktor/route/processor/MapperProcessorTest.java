package com.talevski.viktor.route.processor;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NO_CONTENT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperProcessorTest {
    private static final String ALBERT = "Albert";
    private static final String EINSTEIN = "Einstein";
    private static final String MICHAEL = "Michael";
    private static final String FARADAY = "Faraday";

    private Exchange exchange;
    private MapperProcessor mapperProcessor;

    @BeforeEach
    public void setUp() {
        exchange = new DefaultExchange(new DefaultCamelContext());
        mapperProcessor = new MapperProcessor();
    }

    @Test
    public void shouldSetScientistResponseWithScientistResult() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistPersonalLifeResponse scientistPersonalLifeResponse = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = readJsonFileToObject(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);
        EnrichmentResult enrichmentResult = createEnrichmentResult(scientistPersonalLifeResponse, scientistProfessionalLifeResponse, emptyList());
        exchange.getIn().setBody(enrichmentResult, EnrichmentResult.class);
        exchange.setProperty(FIRST_NAME, ALBERT);
        exchange.setProperty(LAST_NAME, EINSTEIN);
        mapperProcessor.process(exchange);
        ScientistResponse actualScientistResponse = exchange.getIn().getBody(ScientistResponse.class);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldSetScientistResponseWithScientistErrors() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST, ScientistResponse.class);

        EnrichmentResult enrichmentResult = createEnrichmentResult(null, null, asList(createScientistError(NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API), createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API)));
        exchange.getIn().setBody(enrichmentResult, EnrichmentResult.class);
        exchange.setProperty(FIRST_NAME, MICHAEL);
        exchange.setProperty(LAST_NAME, FARADAY);
        mapperProcessor.process(exchange);
        ScientistResponse actualScientistResponse = exchange.getIn().getBody(ScientistResponse.class);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldSetScientistResponseWithScientistResultAndScientistError() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST, ScientistResponse.class);

        ScientistPersonalLifeResponse scientistPersonalLifeResponse = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        EnrichmentResult enrichmentResult = createEnrichmentResult(scientistPersonalLifeResponse, null, asList(createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API)));
        exchange.getIn().setBody(enrichmentResult, EnrichmentResult.class);
        exchange.setProperty(FIRST_NAME, ALBERT);
        exchange.setProperty(LAST_NAME, EINSTEIN);
        mapperProcessor.process(exchange);
        ScientistResponse actualScientistResponse = exchange.getIn().getBody(ScientistResponse.class);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    private EnrichmentResult createEnrichmentResult(ScientistPersonalLifeResponse scientistPersonalLifeResponse, ScientistProfessionalLifeResponse scientistProfessionalLifeResponse, List<ScientistError> scientistErrors) {
        return new EnrichmentResult(scientistPersonalLifeResponse, scientistProfessionalLifeResponse, scientistErrors);
    }

    private ScientistError createScientistError(String errorMessage) {
        ScientistError scientistPersonalLifeError = new ScientistError();
        scientistPersonalLifeError.setErrorCode(EXTERNAL_API_NO_CONTENT);
        scientistPersonalLifeError.setErrorMessage(errorMessage);
        return scientistPersonalLifeError;
    }
}