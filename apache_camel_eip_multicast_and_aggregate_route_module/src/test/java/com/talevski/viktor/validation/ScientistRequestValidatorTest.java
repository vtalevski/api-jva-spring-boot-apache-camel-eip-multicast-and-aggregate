package com.talevski.viktor.validation;

import com.talevski.viktor.model.ScientistRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_CONSTRAINT_VIOLATION_EXCEPTION;
import static com.talevski.viktor.util.JsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFileToObject;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ScientistRequestValidatorTest {
    private static final String EMPTY_STRING = "";

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private ScientistRequestValidator scientistRequestValidator;

    @BeforeEach
    public void setUp() {
        scientistRequestValidator = new ScientistRequestValidator();
    }

    @Test
    public void shouldValidateValidScientistRequest() throws IOException {
        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(EMPTY_STRING)).thenReturn(constraintViolationBuilder);
        boolean actualIsValid = scientistRequestValidator.isValid(scientistRequest, constraintValidatorContext);

        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(EMPTY_STRING);
        assertTrue(actualIsValid);
    }

    @Test
    public void shouldValidateInvalidScientistRequest() throws IOException {
        String expectedValidationErrorMessage = getValidationErrorMessages(FIRST_NAME_VALIDATION_ERROR_MESSAGE)
                .concat(getValidationErrorMessages(LAST_NAME_VALIDATION_ERROR_MESSAGE));

        ScientistRequest scientistRequest = readJsonFileToObject(SCIENTIST_REQUEST_CONSTRAINT_VIOLATION_EXCEPTION, ScientistRequest.class);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(expectedValidationErrorMessage)).thenReturn(constraintViolationBuilder);
        boolean actualIsValid = scientistRequestValidator.isValid(scientistRequest, constraintValidatorContext);

        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(expectedValidationErrorMessage);
        assertFalse(actualIsValid);
    }

    private String getValidationErrorMessages(String specificValidationErrorMessage) {
        return VALIDATION_ERROR_MESSAGE
                .concat(EMPTY_SPACE)
                .concat(specificValidationErrorMessage)
                .concat(SPLITTER);
    }
}