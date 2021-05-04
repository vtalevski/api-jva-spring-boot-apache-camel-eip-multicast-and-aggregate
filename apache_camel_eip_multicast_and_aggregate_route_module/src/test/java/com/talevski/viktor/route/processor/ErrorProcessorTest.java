package com.talevski.viktor.route.processor;

import com.talevski.viktor.exception.CommunicationWithExternalServiceException;
import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.model.ScientistErrorCode;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import org.apache.camel.Exchange;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.http.conn.HttpHostConnectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_BAD_REQUEST;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_GENERIC_ERROR_CODE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NOT_ACCESSIBLE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NO_CONTENT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EXTERNAL_API_IS_DOWN_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.WRONG_URI_ERROR_MESSAGE;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorProcessorTest {
    private static final String TEST_ERROR_MESSAGE = "Test error message.";

    private Exchange exchange;
    private ErrorProcessor errorProcessor;

    @BeforeEach
    public void setUp() {
        exchange = new DefaultExchange(new DefaultCamelContext());
        errorProcessor = new ErrorProcessor();
    }

    @Test
    public void shouldSetScientistErrorIfExceptionCaughtExchangePropertyIsOfHttpOperationFailedExceptionType() throws Exception {
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResultWithScientistError(EXTERNAL_API_BAD_REQUEST, WRONG_URI_ERROR_MESSAGE);

        exchange.setProperty(EXCEPTION_CAUGHT, new HttpOperationFailedException(null, 0, null, null, null, null));
        errorProcessor.process(exchange);
        EnrichmentResult actualEnrichmentResult = exchange.getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorCode(), actualEnrichmentResult.getScientistErrors().get(0).getErrorCode());
        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorMessage(), actualEnrichmentResult.getScientistErrors().get(0).getErrorMessage());
    }

    @Test
    public void shouldSetScientistErrorIfExceptionCaughtExchangePropertyIsOfHttpHostConnectExceptionType() throws Exception {
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResultWithScientistError(EXTERNAL_API_NOT_ACCESSIBLE, EXTERNAL_API_IS_DOWN_ERROR_MESSAGE);

        exchange.setProperty(EXCEPTION_CAUGHT, new HttpHostConnectException(new IOException(), null, null));
        errorProcessor.process(exchange);
        EnrichmentResult actualEnrichmentResult = exchange.getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorCode(), actualEnrichmentResult.getScientistErrors().get(0).getErrorCode());
        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorMessage(), actualEnrichmentResult.getScientistErrors().get(0).getErrorMessage());
    }

    @Test
    public void shouldSetScientistErrorIfExceptionCaughtExchangePropertyIsOfCommunicationWithExternalServiceExceptionType() throws Exception {
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResultWithScientistError(EXTERNAL_API_NO_CONTENT, TEST_ERROR_MESSAGE);

        exchange.setProperty(EXCEPTION_CAUGHT, new CommunicationWithExternalServiceException(TEST_ERROR_MESSAGE));
        errorProcessor.process(exchange);
        EnrichmentResult actualEnrichmentResult = exchange.getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorCode(), actualEnrichmentResult.getScientistErrors().get(0).getErrorCode());
        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorMessage(), actualEnrichmentResult.getScientistErrors().get(0).getErrorMessage());
    }

    @Test
    public void shouldSetScientistErrorIfExceptionCaughtExchangePropertyIsOfExceptionType() throws Exception {
        EnrichmentResult expectedEnrichmentResult = createExpectedEnrichmentResultWithScientistError(EXTERNAL_API_GENERIC_ERROR_CODE, TEST_ERROR_MESSAGE);

        exchange.setProperty(EXCEPTION_CAUGHT, new Exception(TEST_ERROR_MESSAGE));
        errorProcessor.process(exchange);
        EnrichmentResult actualEnrichmentResult = exchange.getIn().getBody(EnrichmentResult.class);

        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorCode(), actualEnrichmentResult.getScientistErrors().get(0).getErrorCode());
        assertEquals(expectedEnrichmentResult.getScientistErrors().get(0).getErrorMessage(), actualEnrichmentResult.getScientistErrors().get(0).getErrorMessage());
    }

    private EnrichmentResult createExpectedEnrichmentResultWithScientistError(ScientistErrorCode scientistErrorCode, String errorMessage) {
        return new EnrichmentResult(new ScientistError(scientistErrorCode, errorMessage));
    }
}