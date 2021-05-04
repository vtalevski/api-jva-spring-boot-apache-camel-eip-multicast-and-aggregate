package com.talevski.viktor.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talevski.viktor.exception.CommunicationWithExternalServiceException;
import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistErrorCode;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import com.talevski.viktor.route.processor.ErrorProcessor;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.http.conn.HttpHostConnectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_BAD_REQUEST;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_GENERIC_ERROR_CODE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NOT_ACCESSIBLE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NO_CONTENT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EXTERNAL_API_IS_DOWN_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.WRONG_URI_ERROR_MESSAGE;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.apache.camel.ExchangePattern.InOut;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;

@ActiveProfiles(value = "it")
@SpringBootTest
@ExtendWith(value = SpringExtension.class)
public class ScientistPersonalLifeEnrichmentRouteTest {
    private static final String ROUTE_SCIENTIST_PERSONAL_LIFE_URI_MOCK = "{{route.scientist.personalLife.uri}}";
    private static final String ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT = "{{route.scientist.personalLife.endpoint}}";
    private static final String TEST_ERROR_MESSAGE = "Test error message.";

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ErrorProcessor errorProcessor;
    @EndpointInject(value = ROUTE_SCIENTIST_PERSONAL_LIFE_URI_MOCK)
    private MockEndpoint scientistPersonalLifeUriMockEndpoint;
    @EndpointInject(value = ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT)
    private ProducerTemplate producerTemplate;

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeResult() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponseObject = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        String expectedScientistPersonalLifeResponseString = objectMapper.writeValueAsString(expectedScientistPersonalLifeResponseObject);
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResult(expectedScientistPersonalLifeResponseObject, null, emptyList());

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(expectedScientistPersonalLifeResponseString, String.class));
        EnrichmentResult actualEnrichmentResult = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult, actualEnrichmentResult);
    }

    @Test
    public void shouldCatchHttpOperationFailedException() throws Exception {
        HttpOperationFailedException expectedHttpOperationFailedException = new HttpOperationFailedException(null, 0, null, null, null, null);
        String expectedErrorMessage = expectedHttpOperationFailedException.getMessage();
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResult(null, null, singletonList(createScientistError(EXTERNAL_API_BAD_REQUEST, WRONG_URI_ERROR_MESSAGE)));

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> {
            throw expectedHttpOperationFailedException;
        });
        mockMapperProcessor(HttpOperationFailedException.class, expectedErrorMessage, expectedEnrichmentResult);
        EnrichmentResult actualEnrichmentResult = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult, actualEnrichmentResult);
    }

    @Test
    public void shouldCatchHttpHostConnectException() throws Exception {
        HttpHostConnectException expectedHttpHostConnectException = new HttpHostConnectException(new IOException(), null, null);
        String expectedErrorMessage = expectedHttpHostConnectException.getMessage();
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResult(null, null, singletonList(createScientistError(EXTERNAL_API_NOT_ACCESSIBLE, EXTERNAL_API_IS_DOWN_ERROR_MESSAGE)));

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> {
            throw expectedHttpHostConnectException;
        });
        mockMapperProcessor(HttpHostConnectException.class, expectedErrorMessage, expectedEnrichmentResult);
        EnrichmentResult actualEnrichmentResult = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult, actualEnrichmentResult);
    }

    @Test
    public void shouldCatchException() throws Exception {
        Exception expectedException = new Exception(TEST_ERROR_MESSAGE);
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResult(null, null, singletonList(createScientistError(EXTERNAL_API_GENERIC_ERROR_CODE, TEST_ERROR_MESSAGE)));

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> {
            throw expectedException;
        });
        mockMapperProcessor(Exception.class, TEST_ERROR_MESSAGE, expectedEnrichmentResult);
        EnrichmentResult actualEnrichmentResult = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult, actualEnrichmentResult);
    }

    @Test
    public void shouldCatchCommunicationWithExternalServiceException() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponseObject = readJsonFileToObject(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);
        String expectedScientistPersonalLifeResponseString = objectMapper.writeValueAsString(expectedScientistPersonalLifeResponseObject);
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResult(null, null, singletonList(createScientistError(EXTERNAL_API_NO_CONTENT, NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API)));

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(expectedScientistPersonalLifeResponseString, String.class));
        mockMapperProcessor(CommunicationWithExternalServiceException.class, NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API, expectedEnrichmentResult);
        EnrichmentResult actualEnrichmentResult = producerTemplate.send(producerTemplate.getDefaultEndpoint(), InOut, exchange -> exchange.getIn().setBody(scientistRequest, ScientistRequest.class)).getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult, actualEnrichmentResult);
    }

    private EnrichmentResult createExpectedEnrichmentResult(ScientistPersonalLifeResponse scientistPersonalLifeResponse, ScientistProfessionalLifeResponse scientistProfessionalLifeResponse, List<ScientistError> scientistErrors) {
        return new EnrichmentResult(scientistPersonalLifeResponse, scientistProfessionalLifeResponse, scientistErrors);
    }

    private ScientistError createScientistError(ScientistErrorCode scientistErrorCode, String errorMessage) {
        return new ScientistError(scientistErrorCode, errorMessage);
    }

    private void mockMapperProcessor(Class exceptionType, String errorMessage, EnrichmentResult expectedEnrichmentResult) throws Exception {
        doAnswer(invocation -> {
            Exchange exchange = invocation.getArgument(0);
            exchange.getIn().setBody(expectedEnrichmentResult, EnrichmentResult.class);
            return null;
        }).when(errorProcessor).process(argThat(exchange -> exchange.getProperty(EXCEPTION_CAUGHT, Exception.class).getClass().equals(exceptionType)
                && exchange.getProperty(EXCEPTION_CAUGHT, Exception.class).getMessage().equals(errorMessage)));
    }
}