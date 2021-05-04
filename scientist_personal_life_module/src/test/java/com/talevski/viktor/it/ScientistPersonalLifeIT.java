package com.talevski.viktor.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talevski.viktor.model.ScientistPersonalLife;
import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistPersonalLifeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static com.talevski.viktor.util.ScientistPersonalLifeConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_NOT_EXIST;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(value = SpringExtension.class)
public class ScientistPersonalLifeIT {
    private static final String SCIENTIST_PERSONAL_LIFE_ENDPOINT = "/scientist-personal-life";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScientistPersonalLifeRepository scientistPersonalLifeRepository;

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeResult() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        ScientistPersonalLife expectedScientistPersonalLife = createExpectedScientistPersonalLife(scientistFirstAndLastName, expectedScientistPersonalLifeResponse);
        when(scientistPersonalLifeRepository.getOne(scientistFirstAndLastName)).thenReturn(expectedScientistPersonalLife);
        ScientistPersonalLifeResponse actualScientistPersonalLifeResponse = callScientistPersonalLifeEndpoint(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponse, actualScientistPersonalLifeResponse);
    }

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeError() throws Exception {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        when(scientistPersonalLifeRepository.getOne(scientistFirstAndLastName)).thenThrow(EntityNotFoundException.class);
        ScientistPersonalLifeResponse actualScientistPersonalLifeResponse = callScientistPersonalLifeEndpoint(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponse, actualScientistPersonalLifeResponse);
    }

    private ScientistPersonalLife createExpectedScientistPersonalLife(String scientistFirstAndLastName, ScientistPersonalLifeResponse scientistPersonalLifeResponse) {
        ScientistPersonalLife scientistPersonalLife = new ScientistPersonalLife();
        scientistPersonalLife.setScientistFirstAndLastName(scientistFirstAndLastName);
        scientistPersonalLife.setScientistPersonalLifeResult(scientistPersonalLifeResponse.getScientistPersonalLifeResult());
        return scientistPersonalLife;
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

