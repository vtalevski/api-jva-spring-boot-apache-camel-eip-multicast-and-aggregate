package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.ScientistResponse;
import com.talevski.viktor.route.ApacheCamelEipMulticastAndAggregateRouteProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.springframework.stereotype.Service;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.PRODUCE_ROUTE_START_ENDPOINT;

@Service
@Slf4j
public class ApacheCamelEipMulticastAndAggregateRouteServiceImpl implements ApacheCamelEipMulticastAndAggregateRouteService {

    @Produce(value = PRODUCE_ROUTE_START_ENDPOINT)
    private ApacheCamelEipMulticastAndAggregateRouteProxy apacheCamelEipMulticastAndAggregateRouteProxy;

    @Override
    public ScientistResponse getScientist(ScientistRequest scientistRequest) {
        log.info("The getScientist method of the ApacheCamelEipAggregateRouteServiceImpl class.");
        ScientistResponse scientistResponse = apacheCamelEipMulticastAndAggregateRouteProxy.getScientistResponse(scientistRequest);
        return scientistResponse;
    }
}
