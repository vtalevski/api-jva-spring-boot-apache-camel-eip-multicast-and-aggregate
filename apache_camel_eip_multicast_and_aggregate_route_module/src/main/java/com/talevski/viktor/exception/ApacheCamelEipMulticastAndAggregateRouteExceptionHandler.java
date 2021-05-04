package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.talevski.viktor.model.ScientistErrorCode.INTERNAL_API_BAD_REQUEST;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.CONSTRAINT_VALIDATOR_SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.SPLITTER;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.VALIDATION_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ApacheCamelEipMulticastAndAggregateRouteExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ScientistResponse> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        log.info("The handleConstraintViolationException method of the ApacheCamelEipAggregateRouteExceptionHandler class.");

        List<String> errorMessages = Stream.of(constraintViolationException.getMessage().split(CONSTRAINT_VALIDATOR_SPLITTER))
                .filter(constraintValidatorErrorMessages -> constraintValidatorErrorMessages.contains(VALIDATION_ERROR_MESSAGE))
                .flatMap(validatorErrorMessages -> Stream.of(validatorErrorMessages.split(SPLITTER)))
                .collect(Collectors.toList());

        ScientistResponse scientistResponse = new ScientistResponse();
        scientistResponse.setScientistErrors(constructScientistErrors(errorMessages));
        return new ResponseEntity<>(scientistResponse, BAD_REQUEST);
    }

    private List<ScientistError> constructScientistErrors(List<String> errorMessages) {
        return errorMessages.stream().map(errorMessage -> {
            ScientistError scientistError = new ScientistError();
            scientistError.setErrorCode(INTERNAL_API_BAD_REQUEST);
            scientistError.setErrorMessage(errorMessage);
            return scientistError;
        }).collect(Collectors.toList());
    }
}
