package com.talevski.viktor.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil {
    public static <T> T readJsonFile(String location, Class<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream(location);
        return objectMapper.readValue(inputStream, type);
    }
}
