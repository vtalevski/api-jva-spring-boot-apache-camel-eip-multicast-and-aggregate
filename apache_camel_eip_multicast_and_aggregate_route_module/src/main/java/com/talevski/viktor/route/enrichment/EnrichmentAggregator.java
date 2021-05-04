package com.talevski.viktor.route.enrichment;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentAggregator implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) return newExchange;

        EnrichmentResult oldExchangeEnrichmentResult = oldExchange.getIn().getBody(EnrichmentResult.class);
        EnrichmentResult newExchangeEnrichmentResult = newExchange.getIn().getBody(EnrichmentResult.class);
        oldExchangeEnrichmentResult.mergeWith(newExchangeEnrichmentResult);
        oldExchange.getIn().setBody(oldExchangeEnrichmentResult);
        return oldExchange;
    }
}
