package com.talevski.viktor.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.model.personalLife.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_ERROR_NOT_EXIST_AND_PROFESSIONAL_SCIENTIST_RESULT;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static com.talevski.viktor.util.TestUtil.readJsonFileToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "it")
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(value = SpringExtension.class)
public class ApacheCamelEipAggregateRouteIT {
    private static final String ROUTE_SCIENTIST_PERSONAL_LIFE_URI_MOCK = "{{route.scientist.personalLife.uri}}";
    private static final String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI_MOCK = "{{route.scientist.professionalLife.uri}}";
    private static final String SCIENTIST_ENDPOINT = "/scientist";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @EndpointInject(value = ROUTE_SCIENTIST_PERSONAL_LIFE_URI_MOCK)
    private MockEndpoint scientistPersonalLifeUriMockEndpoint;
    @EndpointInject(value = ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI_MOCK)
    private MockEndpoint scientistProfessionalLifeUriMockEndpoint;

    @Test
    public void shouldReturnScientistResponseWithScientistResult() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistPersonalLifeResponseString = readJsonFileToString(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        String scientistProfessionalLifeResponseString = readJsonFileToString(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);

        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistPersonalLifeResponseString, String.class));
        scientistProfessionalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistProfessionalLifeResponseString, String.class));
        ScientistResponse actualScientistResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithScientistErrors() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        String scientistPersonalLifeResponseString = readJsonFileToString(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);
        String scientistProfessionalLifeResponseString = readJsonFileToString(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR, ScientistProfessionalLifeResponse.class);

        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistPersonalLifeResponseString, String.class));
        scientistProfessionalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistProfessionalLifeResponseString, String.class));
        ScientistResponse actualScientistResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithPersonalScientistResultAndProfessionalScientistError() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_RESULT_AND_PROFESSIONAL_SCIENTIST_ERROR_NOT_EXIST, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistPersonalLifeResponseString = readJsonFileToString(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        String scientistProfessionalLifeResponseString = readJsonFileToString(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR, ScientistProfessionalLifeResponse.class);

        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistPersonalLifeResponseString, String.class));
        scientistProfessionalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistProfessionalLifeResponseString, String.class));
        ScientistResponse actualScientistResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    @Test
    public void shouldReturnScientistResponseWithProfessionalScientistResultAndPersonalScientistError() throws Exception {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_PERSONAL_SCIENTIST_ERROR_NOT_EXIST_AND_PROFESSIONAL_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistPersonalLifeResponseString = readJsonFileToString(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);
        String scientistProfessionalLifeResponseString = readJsonFileToString(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);

        scientistPersonalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistPersonalLifeResponseString, String.class));
        scientistProfessionalLifeUriMockEndpoint.whenAnyExchangeReceived(exchange -> exchange.getIn().setBody(scientistProfessionalLifeResponseString, String.class));
        ScientistResponse actualScientistResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }

    private ScientistResponse callScientistEndpoint(ScientistRequest scientistRequest) throws Exception {
        return objectMapper.readValue(mockMvc
                        .perform(get(SCIENTIST_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scientistRequest)))
                        .andExpect(status()
                                .isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ScientistResponse.class);
    }
}
