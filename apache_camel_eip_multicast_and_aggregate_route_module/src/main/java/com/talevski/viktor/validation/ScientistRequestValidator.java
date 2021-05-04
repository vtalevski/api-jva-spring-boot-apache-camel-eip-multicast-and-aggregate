package com.talevski.viktor.validation;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ValidationError;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME_VALIDATION_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.VALIDATION_ERROR_MESSAGE;
import static java.lang.Character.isLowerCase;

@Component
public class ScientistRequestValidator implements ConstraintValidator<ValidatedScientistRequest, ScientistRequest> {

    @Override
    public boolean isValid(ScientistRequest scientistRequest, ConstraintValidatorContext constraintValidatorContext) {
        ValidationError validationError = new ValidationError(true, "");
        validateFirstName(scientistRequest, validationError);
        validateLastName(scientistRequest, validationError);

        if (!validationError.isValid())
            constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(validationError.getValidationErrorMessages()).addConstraintViolation();
        return validationError.isValid();
    }

    private void validateFirstName(ScientistRequest scientistRequest, ValidationError validationError) {
        if (isLowerCase(scientistRequest.getFirstName().charAt(0))) {
            validationError.setValid(false);
            validationError.setValidationErrorMessages(getValidationErrorMessages(validationError.getValidationErrorMessages(),
                    FIRST_NAME_VALIDATION_ERROR_MESSAGE));
        }
    }

    private void validateLastName(ScientistRequest scientistRequest, ValidationError validationError) {
        if (isLowerCase(scientistRequest.getLastName().charAt(0))) {
            validationError.setValid(false);
            validationError.setValidationErrorMessages(getValidationErrorMessages(validationError.getValidationErrorMessages(),
                    LAST_NAME_VALIDATION_ERROR_MESSAGE));
        }
    }

    private String getValidationErrorMessages(String validationErrorMessages, String specificValidationErrorMessage) {
        return validationErrorMessages
                .concat(VALIDATION_ERROR_MESSAGE)
                .concat(EMPTY_SPACE)
                .concat(specificValidationErrorMessage)
                .concat(SPLITTER);
    }
}
