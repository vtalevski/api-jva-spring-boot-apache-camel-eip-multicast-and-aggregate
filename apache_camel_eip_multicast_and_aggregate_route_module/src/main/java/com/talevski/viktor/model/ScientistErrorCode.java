package com.talevski.viktor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@AllArgsConstructor
@Getter
public enum ScientistErrorCode {
    EXTERNAL_API_NO_CONTENT(OK),
    INTERNAL_API_BAD_REQUEST(BAD_REQUEST),
    EXTERNAL_API_BAD_REQUEST(BAD_REQUEST),
    EXTERNAL_API_NOT_ACCESSIBLE(INTERNAL_SERVER_ERROR),
    EXTERNAL_API_GENERIC_ERROR_CODE(SERVICE_UNAVAILABLE);

    private HttpStatus httpStatus;
}
