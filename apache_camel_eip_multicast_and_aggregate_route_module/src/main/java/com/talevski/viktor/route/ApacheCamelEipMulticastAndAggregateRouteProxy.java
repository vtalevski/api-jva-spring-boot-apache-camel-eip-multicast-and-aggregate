package com.talevski.viktor.route;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;

@FunctionalInterface
public interface ApacheCamelEipMulticastAndAggregateRouteProxy {
    ScientistResponse getScientistResponse(ScientistRequest scientistRequest);
}
