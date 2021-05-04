package com.talevski.viktor.route;

import com.talevski.viktor.exception.CommunicationWithExternalServiceException;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.model.professionalLife.ScientistProfessionalLifeResponse;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import com.talevski.viktor.route.processor.ErrorProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI;
import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.http.common.HttpMethods.POST;
import static org.apache.camel.model.dataformat.JsonLibrary.Jackson;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Component
public class ScientistProfessionalLifeEnrichmentRoute extends RouteBuilder {
    private ErrorProcessor errorProcessor;

    public ScientistProfessionalLifeEnrichmentRoute(ErrorProcessor errorProcessor) {
        this.errorProcessor = errorProcessor;
    }

    @Override
    public void configure() {
        // @formatter:off
        from(ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT).routeId(ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID)
            .log(INFO, log, "The configure method of the ScientistProfessionalLifeEnrichmentRoute class.")
            .setHeader(HTTP_METHOD, constant(POST))
            .setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
            .doTry()
                .marshal().json(Jackson, ScientistRequest.class)
                .to(ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI)
                .unmarshal().json(Jackson, ScientistProfessionalLifeResponse.class)
                .choice()
                    .when(simple("${body.scientistProfessionalLifeError} != null"))
                        .throwException(new CommunicationWithExternalServiceException(NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API))
                    .otherwise()
                        .process(this::mapToEnrichment)
                .endChoice()
            .endDoTry()
            .doCatch(Exception.class)
                .process(errorProcessor)
            .end()
        .end();
        // @formatter:on
    }

    private void mapToEnrichment(Exchange exchange) {
        exchange.getIn().setBody(new EnrichmentResult(exchange.getIn().getBody(ScientistProfessionalLifeResponse.class)));
    }
}
