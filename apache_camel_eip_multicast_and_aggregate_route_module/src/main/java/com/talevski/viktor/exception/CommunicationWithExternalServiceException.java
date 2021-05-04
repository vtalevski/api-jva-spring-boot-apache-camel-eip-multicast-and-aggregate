package com.talevski.viktor.exception;

public class CommunicationWithExternalServiceException extends RuntimeException {
    private static final long serialVersionUID = 5888729357295311719L;

    public CommunicationWithExternalServiceException(String errorMessage) {
        super(errorMessage);
    }
}
