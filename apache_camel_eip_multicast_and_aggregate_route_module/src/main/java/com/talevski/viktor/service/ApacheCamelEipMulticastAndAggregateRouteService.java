package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;

public interface ApacheCamelEipMulticastAndAggregateRouteService {
    ScientistResponse getScientist(ScientistRequest scientistRequest);
}
