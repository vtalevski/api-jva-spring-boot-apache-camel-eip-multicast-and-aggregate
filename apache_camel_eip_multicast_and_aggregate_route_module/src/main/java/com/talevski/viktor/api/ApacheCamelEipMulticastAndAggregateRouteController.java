package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.service.ApacheCamelEipMulticastAndAggregateRouteService;
import com.talevski.viktor.validation.ValidatedScientistRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@Slf4j
public class ApacheCamelEipMulticastAndAggregateRouteController {
    private ApacheCamelEipMulticastAndAggregateRouteService apacheCamelEipMulticastAndAggregateRouteService;

    public ApacheCamelEipMulticastAndAggregateRouteController(ApacheCamelEipMulticastAndAggregateRouteService apacheCamelEipMulticastAndAggregateRouteService) {
        this.apacheCamelEipMulticastAndAggregateRouteService = apacheCamelEipMulticastAndAggregateRouteService;
    }

    @GetMapping(value = "/scientist")
    public ResponseEntity<ScientistResponse> getScientist(@ValidatedScientistRequest @RequestBody ScientistRequest scientistRequest) {
        log.info("The getScientist method of the ApacheCamelEipAggregateRouteController class.");
        ScientistResponse scientistResponse = apacheCamelEipMulticastAndAggregateRouteService.getScientist(scientistRequest);
        HttpStatus httpStatus = getHttpStatus(scientistResponse.getScientistErrors());
        return new ResponseEntity<>(scientistResponse, httpStatus);
    }

    private HttpStatus getHttpStatus(List<ScientistError> scientistErrors) {
        HttpStatus httpStatus = OK;
        if (scientistErrors != null) {
            httpStatus = scientistErrors.stream()
                    .map(ScientistError::getErrorCode)
                    .max(Enum::compareTo)
                    .get()
                    .getHttpStatus();
        }
        return httpStatus;
    }
}
