package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.service.ScientistProfessionalLifeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
public class ScientistProfessionalLifeController {
    private ScientistProfessionalLifeService scientistProfessionalLifeService;

    public ScientistProfessionalLifeController(ScientistProfessionalLifeService scientistProfessionalLifeService) {
        this.scientistProfessionalLifeService = scientistProfessionalLifeService;
    }

    @PostMapping(value = "/scientist-professional-life")
    public ResponseEntity<ScientistProfessionalLifeResponse> getScientistProfessionalLife(@RequestBody ScientistRequest scientistRequest) {
        log.info("The getScientistProfessionalLife method of the ScientistProfessionalLifeController class.");
        return new ResponseEntity<>(scientistProfessionalLifeService.getScientistProfessionalLife(scientistRequest), OK);
    }
}
