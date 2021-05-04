package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistPersonalLifeError;
import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static com.talevski.viktor.util.ScientistPersonalLifeConstants.NO_PERSONAL_RECORDS_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ScientistPersonalLifeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ScientistPersonalLifeResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        log.info("The handleEntityNotFoundException method of the ScientistPersonalLifeExceptionHandler class.");
        ScientistPersonalLifeResponse scientistPersonalLifeResponse = new ScientistPersonalLifeResponse();
        scientistPersonalLifeResponse.setScientistPersonalLifeError(new ScientistPersonalLifeError(NO_PERSONAL_RECORDS_FOUND));
        return new ResponseEntity<>(scientistPersonalLifeResponse, OK);
    }
}
