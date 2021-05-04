package com.talevski.viktor.route.enrichment;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class EnrichmentAggregatorTest {

    @InjectMocks
    private EnrichmentAggregator enrichmentAggregator;

    @Test
    public void shouldMergeOldExchangeAndNewExchange() {
        Exchange oldExchange = mock(Exchange.class);
        Exchange newExchange = mock(Exchange.class);

        EnrichmentResult oldEnrichmentResult = mock(EnrichmentResult.class);
        EnrichmentResult newEnrichmentResult = mock(EnrichmentResult.class);

        mockExchange(oldEnrichmentResult, oldExchange);
        mockExchange(newEnrichmentResult, newExchange);

        enrichmentAggregator.aggregate(oldExchange, newExchange);
        verify(oldEnrichmentResult).mergeWith(newEnrichmentResult);
    }

    @Test
    public void shouldReturnNewExchange() {
        Exchange newExchange = mock(Exchange.class);

        Exchange actualExchange = enrichmentAggregator.aggregate(null, newExchange);
        assertEquals(newExchange, actualExchange);
    }

    private void mockExchange(EnrichmentResult enrichmentResult, Exchange exchange) {
        Message message = mock(Message.class);

        when(message.getBody(EnrichmentResult.class)).thenReturn(enrichmentResult);
        when(exchange.getIn()).thenReturn(message);
    }
}