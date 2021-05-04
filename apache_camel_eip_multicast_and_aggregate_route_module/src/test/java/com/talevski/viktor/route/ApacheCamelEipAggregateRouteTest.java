package com.talevski.viktor.route;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import com.talevski.viktor.route.processor.MapperProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NO_CONTENT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_START_ID;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_ERROR_NOT_EXIST_AND_PROFESSIONAL_SCIENTIST_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static java.util.Arrays.asList;
import static org.apache.camel.ExchangePattern.InOut;
import static org.apache.camel.builder.AdviceWithRouteBuilder.adviceWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@ActiveProfiles(value = "it")
@DirtiesContext(classMode = AFTER_CLASS)
@SpringBootTest
@ExtendWith(value = SpringExtension.class)
public class ApacheCamelEipAggregateRouteTest {
    private static final String ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT_MOCK = "{{route.scientist.personalLife.mockEndpoint}}";
    private static final String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT_MOCK = "{{route.scientist.professionalLife.mockEndpoint}}";
    private static final String ROUTE_START_ENDPOINT = "{{route.start.endpoint}}";

    @Mock
    private MapperProcessor mapperProcessor;
    @EndpointInject(value = ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT_MOCK)
    private MockEndpoint scientistPersonalLifeMockEndpoint;
    @EndpointInject(value = ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT_MOCK)
    private MockEndpoint scientistProfessionalLifeMockEndpoint;
    @EndpointInject(value = ROUTE_START_ENDPOINT)
    private ProducerTemplate producerTemplate;
    @Autowired
    private CamelContext camelContext;

    @BeforeEach
    public void setUp() throws Exception {
        adviceWith(camelContext, ROUTE_START_ID, route -> {
            route.interceptSendToEndpoint(ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT).skipSendToOriginalEndpoint().to(ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT_MOCK);
            route.interceptSendToEndpoint(ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT).skipSendToOriginalEndpoint().to(ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT_MOCK);
        }).autoStartup(true);
    }

    @AfterEach
    public void cleanUp() {
        scientistPersonalLifeMockEndpoint.reset();
        scientistProfessionalLifeMockEndpoint.reset();
    }

    @Test
    public void shouldReturnScientistResponseWithScientistResult() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        ScientistPersonalLifeResponse scientistPersonalLifeResponse = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = readJsonFileToObject(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);
        scientistPersonalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(scientistPersonalLifeResponse), EnrichmentResult.class));
        scientistProfessionalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(scientistProfessionalLifeResponse), EnrichmentResult.class));
        ScientistResponse actualScientistResponse = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(ScientistResponse.class);

        assertScientistRequestAndScientistResponse(scientistRequest, expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithScientistErrors() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        scientistPersonalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(createScientistError(NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API)), EnrichmentResult.class));
        scientistProfessionalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API)), EnrichmentResult.class));
        mockMapperProcessor(createEnrichmentResult(null, null, asList(createScientistError(NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API), createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API))), expectedScientistResponse);
        ScientistResponse actualScientistResponse = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(ScientistResponse.class);

        assertScientistRequestAndScientistResponse(scientistRequest, expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithPersonalScientistResultAndProfessionalScientistError() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        ScientistPersonalLifeResponse scientistPersonalLifeResponse = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        scientistPersonalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(scientistPersonalLifeResponse), EnrichmentResult.class));
        scientistProfessionalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API)), EnrichmentResult.class));
        mockMapperProcessor(createEnrichmentResult(scientistPersonalLifeResponse, null, asList(createScientistError(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API))), expectedScientistResponse);
        ScientistResponse actualScientistResponse = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(ScientistResponse.class);

        assertScientistRequestAndScientistResponse(scientistRequest, expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithProfessionalScientistResultAndPersonalScientistError() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_ERROR_NOT_EXIST_AND_PROFESSIONAL_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = readJsonFileToObject(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);
        scientistPersonalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(createScientistError(NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API)), EnrichmentResult.class));
        scientistProfessionalLifeMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(new EnrichmentResult(scientistProfessionalLifeResponse), EnrichmentResult.class));
        mockMapperProcessor(createEnrichmentResult(null, scientistProfessionalLifeResponse, asList(createScientistError(NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API))), expectedScientistResponse);
        ScientistResponse actualScientistResponse = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(ScientistResponse.class);

        assertScientistRequestAndScientistResponse(scientistRequest, expectedScientistResponse, actualScientistResponse);
    }

    private ScientistError createScientistError(String errorMessage) {
        ScientistError scientistPersonalLifeError = new ScientistError();
        scientistPersonalLifeError.setErrorCode(EXTERNAL_API_NO_CONTENT);
        scientistPersonalLifeError.setErrorMessage(errorMessage);
        return scientistPersonalLifeError;
    }

    private EnrichmentResult createEnrichmentResult(ScientistPersonalLifeResponse scientistPersonalLifeResponse, ScientistProfessionalLifeResponse scientistProfessionalLifeResponse, List<ScientistError> scientistErrors) {
        EnrichmentResult expectedEnrichmentResult = new EnrichmentResult();
        expectedEnrichmentResult.setScientistPersonalLifeResponse(scientistPersonalLifeResponse);
        expectedEnrichmentResult.setScientistProfessionalLifeResponse(scientistProfessionalLifeResponse);
        expectedEnrichmentResult.setScientistErrors(scientistErrors);
        return expectedEnrichmentResult;
    }

    private void mockMapperProcessor(EnrichmentResult expectedEnrichmentResult, ScientistResponse expectedScientistResponse) throws Exception {
        doAnswer(invocation -> {
            Exchange exchange = invocation.getArgument(0);
            exchange.getIn().setBody(expectedScientistResponse, ScientistResponse.class);
            return null;
        }).when(mapperProcessor).process(argThat(exchange -> exchange.getIn().getBody(EnrichmentResult.class).equals(expectedEnrichmentResult)));
    }

    private void assertScientistRequestAndScientistResponse(ScientistRequest scientistRequest, ScientistResponse expectedScientistResponse, ScientistResponse actualScientistResponse) throws InterruptedException {
        scientistPersonalLifeMockEndpoint.expectedMessageCount(1);
        scientistPersonalLifeMockEndpoint.expectedHeaderReceived(FIRST_NAME, scientistRequest.getFirstName());
        scientistPersonalLifeMockEndpoint.expectedHeaderReceived(LAST_NAME, scientistRequest.getLastName());
        scientistPersonalLifeMockEndpoint.expectedBodiesReceived(scientistRequest);
        scientistPersonalLifeMockEndpoint.assertIsSatisfied();
        scientistProfessionalLifeMockEndpoint.expectedMessageCount(1);
        scientistProfessionalLifeMockEndpoint.expectedHeaderReceived(FIRST_NAME, scientistRequest.getFirstName());
        scientistProfessionalLifeMockEndpoint.expectedHeaderReceived(LAST_NAME, scientistRequest.getLastName());
        scientistProfessionalLifeMockEndpoint.expectedBodiesReceived(scientistRequest);
        scientistProfessionalLifeMockEndpoint.assertIsSatisfied();
        assertEquals(expectedScientistResponse, actualScientistResponse);
    }
}