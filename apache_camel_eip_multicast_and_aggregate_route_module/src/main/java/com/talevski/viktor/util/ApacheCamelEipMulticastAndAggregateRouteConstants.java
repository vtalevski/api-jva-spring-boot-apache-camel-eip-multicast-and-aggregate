package com.talevski.viktor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApacheCamelEipMulticastAndAggregateRouteConstants {
    public static final String VALIDATION_ERROR_MESSAGE = "Validation error!";
    public static final String FIRST_NAME_VALIDATION_ERROR_MESSAGE = "The first name must start with an upper case letter.";
    public static final String LAST_NAME_VALIDATION_ERROR_MESSAGE = "The last name must start with an upper case letter.";
    public static final String CONSTRAINT_VALIDATOR_SPLITTER = "getScientist.scientistRequest: ";
    public static final String EMPTY_SPACE = " ";
    public static final String SPLITTER = " - ";

    public static final String WRONG_URI_ERROR_MESSAGE = "Wrong URI for external API.";
    public static final String EXTERNAL_API_IS_DOWN_ERROR_MESSAGE = "External API is down.";
    public static final String NO_VALUE_FROM_EXTERNAL_PERSONAL_SCIENTIST_API = "No personal records found for particular scientist.";
    public static final String NO_VALUE_FROM_EXTERNAL_PROFESSIONAL_SCIENTIST_API = "No professional records found for particular scientist.";

    public static final String PRODUCE_ROUTE_START_ENDPOINT = "{{route.start.endpoint}}";
    public static String ORCHESTRATION_ROUTE_START_ENDPOINT;
    public static String ROUTE_START_ID;
    public static String ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT;
    public static String ROUTE_SCIENTIST_PERSONAL_LIFE_ID;
    public static String ROUTE_SCIENTIST_PERSONAL_LIFE_URI;
    public static String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT;
    public static String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID;
    public static String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI;
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public ApacheCamelEipMulticastAndAggregateRouteConstants(@Value(value = "${route.start.endpoint}") String ORCHESTRATION_ROUTE_START_ENDPOINT,
                                                             @Value(value = "${route.start.id}") String ROUTE_START_ID,
                                                             @Value(value = "${route.scientist.personalLife.endpoint}") String ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT,
                                                             @Value(value = "${route.scientist.personalLife.id}") String ROUTE_SCIENTIST_PERSONAL_LIFE_ID,
                                                             @Value(value = "${route.scientist.personalLife.uri}") String ROUTE_SCIENTIST_PERSONAL_LIFE_URI,
                                                             @Value(value = "${route.scientist.professionalLife.endpoint}") String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT,
                                                             @Value(value = "${route.scientist.professionalLife.id}") String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID,
                                                             @Value(value = "${route.scientist.professionalLife.uri}") String ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI) {
        this.ORCHESTRATION_ROUTE_START_ENDPOINT = ORCHESTRATION_ROUTE_START_ENDPOINT;
        this.ROUTE_START_ID = ROUTE_START_ID;
        this.ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT = ROUTE_SCIENTIST_PERSONAL_LIFE_ENDPOINT;
        this.ROUTE_SCIENTIST_PERSONAL_LIFE_ID = ROUTE_SCIENTIST_PERSONAL_LIFE_ID;
        this.ROUTE_SCIENTIST_PERSONAL_LIFE_URI = ROUTE_SCIENTIST_PERSONAL_LIFE_URI;
        this.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT = ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT;
        this.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID = ROUTE_SCIENTIST_PROFESSIONAL_LIFE_ID;
        this.ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI = ROUTE_SCIENTIST_PROFESSIONAL_LIFE_URI;
    }
}
