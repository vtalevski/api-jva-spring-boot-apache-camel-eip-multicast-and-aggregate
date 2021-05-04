package com.talevski.viktor.ct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.talevski.viktor.model.ScientistPersonalLife;
import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistPersonalLifeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.talevski.viktor.util.MainJsonFilesPaths.SCIENTISTS;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "ct")
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(value = SpringExtension.class)
public class ScientistPersonalLifeCT {
    private static final String SCIENTIST_PERSONAL_LIFE_ENDPOINT = "/scientist-personal-life";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ScientistPersonalLifeRepository scientistPersonalLifeRepository;

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeResult() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);

        populateEmbeddedPostgresql();
        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        ScientistPersonalLifeResponse actualScientistPersonalLifeResponse = callScientistPersonalLifeEndpoint(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponse, actualScientistPersonalLifeResponse);
    }

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeError() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);

        populateEmbeddedPostgresql();
        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        ScientistPersonalLifeResponse actualScientistPersonalLifeResponse = callScientistPersonalLifeEndpoint(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponse, actualScientistPersonalLifeResponse);
    }

    private void populateEmbeddedPostgresql() {
        InputStream inputStream = TypeReference.class.getResourceAsStream(SCIENTISTS);
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, ScientistPersonalLife.class);

        try {
            List<ScientistPersonalLife> scientistPersonalLifeList = objectMapper.readValue(inputStream, collectionType);
            scientistPersonalLifeRepository.saveAll(scientistPersonalLifeList);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assertEquals(3, scientistPersonalLifeRepository.findAll().size());
    }

    private ScientistPersonalLifeResponse callScientistPersonalLifeEndpoint(ScientistRequest scientistRequest) throws Exception {
        return objectMapper.readValue(mockMvc
                        .perform(post(SCIENTIST_PERSONAL_LIFE_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scientistRequest)))
                        .andExpect(status()
                                .isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ScientistPersonalLifeResponse.class);
    }
}
