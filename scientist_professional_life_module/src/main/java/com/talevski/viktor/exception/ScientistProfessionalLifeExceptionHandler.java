package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistProfessionalLifeError;
import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static com.talevski.viktor.util.ScientistProfessionalLifeConstants.NO_PROFESSIONAL_RECORDS_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ScientistProfessionalLifeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ScientistProfessionalLifeResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        log.info("The handleEntityNotFoundException method of the ScientistProfessionalLifeExceptionHandler class.");
        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = new ScientistProfessionalLifeResponse();
        scientistProfessionalLifeResponse.setScientistProfessionalLifeError(new ScientistProfessionalLifeError(NO_PROFESSIONAL_RECORDS_FOUND));
        return new ResponseEntity<>(scientistProfessionalLifeResponse, OK);
    }
}
