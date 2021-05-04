package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.CONSTRAINT_VALIDATOR_SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_CONSTRAINT_VIOLATION_EXCEPTION;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ApacheCamelEipAggregateRouteExceptionHandlerTest {

    @Mock
    private ConstraintViolationException constraintViolationException;
    @InjectMocks
    private ApacheCamelEipMulticastAndAggregateRouteExceptionHandler apacheCamelEipMulticastAndAggregateRouteExceptionHandler;

    @Test
    public void shouldHandleConstraintViolationException() throws IOException {
        ScientistResponse expectedScientistResponse = readJsonFileToObject(SCIENTIST_RESPONSE_WITH_SCIENTIST_ERRORS_CONSTRAINT_VIOLATION_EXCEPTION, ScientistResponse.class);
        ResponseEntity<ScientistResponse> expectedScientistResponseResponseEntity = ResponseEntity.badRequest().body(expectedScientistResponse);

        String constraintViolationExceptionMessage = CONSTRAINT_VALIDATOR_SPLITTER
                .concat(VALIDATION_ERROR_MESSAGE).concat(EMPTY_SPACE)
                .concat(FIRST_NAME_VALIDATION_ERROR_MESSAGE).concat(SPLITTER)
                .concat(VALIDATION_ERROR_MESSAGE).concat(EMPTY_SPACE)
                .concat(LAST_NAME_VALIDATION_ERROR_MESSAGE).concat(SPLITTER);
        when(constraintViolationException.getMessage()).thenReturn(constraintViolationExceptionMessage);
        ResponseEntity<ScientistResponse> actualScientistResponseResponseEntity = apacheCamelEipMulticastAndAggregateRouteExceptionHandler.handleConstraintViolationException(constraintViolationException);

        assertEquals(expectedScientistResponseResponseEntity, actualScientistResponseResponseEntity);
    }
}