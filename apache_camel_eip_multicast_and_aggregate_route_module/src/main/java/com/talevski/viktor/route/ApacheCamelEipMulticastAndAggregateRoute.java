package com.talevski.viktor.route;

import com.talevski.viktor.route.enrichment.EnrichmentAggregator;
import com.talevski.viktor.route.processor.MapperProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.FIRST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.LAST_NAME;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ORCHESTRATION_ROUTE_START_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_START_ID;
import static org.apache.camel.LoggingLevel.INFO;

@Component
public class ApacheCamelEipMulticastAndAggregateRoute extends RouteBuilder {
    private ExecutorService executorService;
    private EnrichmentAggregator enrichmentAggregator;
    private MapperProcessor mapperProcessor;

    public ApacheCamelEipMulticastAndAggregateRoute(ExecutorService executorService, EnrichmentAggregator enrichmentAggregator, MapperProcessor mapperProcessor) {
        this.executorService = executorService;
        this.enrichmentAggregator = enrichmentAggregator;
        this.mapperProcessor = mapperProcessor;
    }

    @Override
    public void configure() {
        // @formatter:off
        from(ORCHESTRATION_ROUTE_START_ENDPOINT).routeId(ROUTE_START_ID)
            .log(INFO, log, "The configure method of the ApacheCamelEipAggregateRoute class.")
            .setProperty(FIRST_NAME, simple("${body.firstName}"))
            .setProperty(LAST_NAME, simple("${body.lastName}"))
            .multicast(enrichmentAggregator, true).executorService(executorService).stopOnException().shareUnitOfWork()
                .to(ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT, ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT)
            .end()
            .process(mapperProcessor)
        .end();
        // @formatter:on
    }
}
