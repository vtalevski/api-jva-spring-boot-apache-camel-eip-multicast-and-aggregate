package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.route.ApacheCamelEipMulticastAndAggregateRouteProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ApacheCamelEipAggregateRouteServiceImplTest {

    @Mock
    private ApacheCamelEipMulticastAndAggregateRouteProxy apacheCamelEipMulticastAndAggregateRouteProxy;
    @InjectMocks
    private ApacheCamelEipMulticastAndAggregateRouteServiceImpl apacheCamelEipAggregateRouteServiceImpl;

    @Test
    public void shouldReturnScientistResponse() throws IOException {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_RESULT, ScientistResponse.class);

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        when(apacheCamelEipMulticastAndAggregateRouteProxy.getScientistResponse(scientistRequest)).thenReturn(expectedScientistResponse);
        ScientistResponse actualScientistResponse = apacheCamelEipAggregateRouteServiceImpl.getScientist(scientistRequest);

        assertEquals(expectedScientistResponse, actualScientistResponse);
    }
}