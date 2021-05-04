package com.talevski.viktor.route.processor;

import com.talevski.viktor.exception.CommunicationWithExternalServiceException;
import com.talevski.viktor.model.ScientistError;
import com.talevski.viktor.route.enrichment.EnrichmentResult;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.stereotype.Component;

import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_BAD_REQUEST;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_GENERIC_ERROR_CODE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NOT_ACCESSIBLE;
import static com.talevski.viktor.model.ScientistErrorCode.EXTERNAL_API_NO_CONTENT;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.EXTERNAL_API_IS_DOWN_ERROR_MESSAGE;
import static com.talevski.viktor.util.ApacheCamelEipMulticastAndAggregateRouteConstants.WRONG_URI_ERROR_MESSAGE;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

@Component
public class ErrorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
        EnrichmentResult enrichmentResult = new EnrichmentResult(setError(exception));
        exchange.getIn().setBody(enrichmentResult);
    }

    private ScientistError setError(Exception exception) {
        ScientistError error = new ScientistError();
        if (exception.getClass().equals(HttpOperationFailedException.class)) {
            error.setErrorCode(EXTERNAL_API_BAD_REQUEST);
            error.setErrorMessage(WRONG_URI_ERROR_MESSAGE);
        } else if (exception.getClass().equals(HttpHostConnectException.class)) {
            error.setErrorCode(EXTERNAL_API_NOT_ACCESSIBLE);
            error.setErrorMessage(EXTERNAL_API_IS_DOWN_ERROR_MESSAGE);
        } else if (exception.getClass().equals(CommunicationWithExternalServiceException.class)) {
            error.setErrorCode(EXTERNAL_API_NO_CONTENT);
            error.setErrorMessage(exception.getMessage());
        } else {
            error.setErrorCode(EXTERNAL_API_GENERIC_ERROR_CODE);
            error.setErrorMessage(exception.getMessage());
        }
        return error;
    }
}
