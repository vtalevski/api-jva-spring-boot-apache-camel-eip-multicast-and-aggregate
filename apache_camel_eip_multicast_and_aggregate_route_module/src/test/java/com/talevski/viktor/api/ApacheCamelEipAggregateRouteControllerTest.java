package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.service.ApacheCamelEipMulticastAndAggregateRouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ApacheCamelEipAggregateRouteControllerTest {

    @Mock
    private ApacheCamelEipMulticastAndAggregateRouteService apacheCamelEipMulticastAndAggregateRouteService;
    @InjectMocks
    private ApacheCamelEipMulticastAndAggregateRouteController apacheCamelEipMulticastAndAggregateRouteController;

    @Test
    public void shouldReturnScientistResponseWithScientistResult() throws IOException {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT, ScientistResponse.class);
        ResponseEntity<ScientistResponse> expectedScientistResponseResponseEntity = ResponseEntity.ok(expectedScientistResponse);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        when(apacheCamelEipMulticastAndAggregateRouteService.getScientist(scientistRequest)).thenReturn(expectedScientistResponse);
        ResponseEntity<ScientistResponse> actualScientistResponseResponseEntity = apacheCamelEipMulticastAndAggregateRouteController.getScientist(scientistRequest);

        assertEquals(expectedScientistResponseResponseEntity, actualScientistResponseResponseEntity);
    }

    @Test
    public void shouldReturnScientistResponseWithScientistErrors() throws IOException {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_NOT_EXIST, ScientistResponse.class);
        ResponseEntity<ScientistResponse> expectedScientistResponseResponseEntity = ResponseEntity.ok(expectedScientistResponse);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        when(apacheCamelEipMulticastAndAggregateRouteService.getScientist(scientistRequest)).thenReturn(expectedScientistResponse);
        ResponseEntity<ScientistResponse> actualScientistResponseResponseEntity = apacheCamelEipMulticastAndAggregateRouteController.getScientist(scientistRequest);

        assertEquals(expectedScientistResponseResponseEntity, actualScientistResponseResponseEntity);
    }
}