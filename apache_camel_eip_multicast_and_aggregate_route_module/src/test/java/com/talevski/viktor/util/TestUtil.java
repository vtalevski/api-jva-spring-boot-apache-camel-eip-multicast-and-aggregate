package com.talevski.viktor.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFileToObject(String location, Class<T> type) throws IOException {
        InputStream inputStream = TypeReference.class.getResourceAsStream(location);
        return objectMapper.readValue(inputStream, type);
    }

    public static <T> String readJsonFileToString(String location, Class<T> type) throws IOException {
        T scientistLifeObject = readJsonFileToObject(location, type);
        return objectMapper.writeValueAsString(scientistLifeObject);
    }
}
