package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.service.ScientistPersonalLifeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
public class ScientistPersonalLifeController {
    private ScientistPersonalLifeService scientistPersonalLifeService;

    public ScientistPersonalLifeController(ScientistPersonalLifeService scientistPersonalLifeService) {
        this.scientistPersonalLifeService = scientistPersonalLifeService;
    }

    @PostMapping(value = "/scientist-personal-life")
    public ResponseEntity<ScientistPersonalLifeResponse> getScientistPersonalLife(@RequestBody ScientistRequest scientistRequest) {
        log.info("The getScientistPersonalLife method of the ScientistPersonalLifeController class.");
        return new ResponseEntity<>(scientistPersonalLifeService.getScientistPersonalLife(scientistRequest), OK);
    }
}
