package com.talevski.viktor.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talevski.viktor.model.ScientistProfessionalLife;
import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistProfessionalLifeRepository;
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

import static com.talevski.viktor.util.ScientistProfessionalLifeConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT;
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
public class ScientistProfessionalLifeIT {
    private static final String SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT = "/scientist-professional-life";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScientistProfessionalLifeRepository scientistProfessinalLifeRepository;

    @Test
    public void shouldReturnScientistProfessionalLifeResponseWithScientistProfessionalLifeResult() throws Exception {
        ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse = readJsonFile(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        ScientistProfessionalLife expectedScientistProfessionalLife = createExpectedScientistProfessionalLife(scientistFirstAndLastName, expectedScientistProfessionalLifeResponse);
        when(scientistProfessinalLifeRepository.getOne(scientistFirstAndLastName)).thenReturn(expectedScientistProfessionalLife);
        ScientistProfessionalLifeResponse actualScientistProfessionalLifeResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistProfessionalLifeResponse, actualScientistProfessionalLifeResponse);
    }

    @Test
    public void shouldReturnScientistProfessionalLifeResponseWithScientistProfessionalLifeError() throws Exception {
        ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse = readJsonFile(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR, ScientistProfessionalLifeResponse.class);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_NOT_EXIST, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        when(scientistProfessinalLifeRepository.getOne(scientistFirstAndLastName)).thenThrow(EntityNotFoundException.class);
        ScientistProfessionalLifeResponse actualScientistProfessionalLifeResponse = callScientistEndpoint(scientistRequest);

        assertEquals(expectedScientistProfessionalLifeResponse, actualScientistProfessionalLifeResponse);
    }

    private ScientistProfessionalLife createExpectedScientistProfessionalLife(String scientistFirstAndLastName, ScientistProfessionalLifeResponse scientistProfessionalLifeResponse) {
        ScientistProfessionalLife scientistProfessionalLife = new ScientistProfessionalLife();
        scientistProfessionalLife.setScientistFirstAndLastName(scientistFirstAndLastName);
        scientistProfessionalLife.setScientistProfessionalLifeResult(scientistProfessionalLifeResponse.getScientistProfessionalLifeResult());
        return scientistProfessionalLife;
    }

    private ScientistProfessionalLifeResponse callScientistEndpoint(ScientistRequest scientistRequest) throws Exception {
        return objectMapper.readValue(mockMvc
                        .perform(post(SCIENTIST_PROFESSIONAL_LIFE_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(scientistRequest)))
                        .andExpect(status()
                                .isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ScientistProfessionalLifeResponse.class);
    }
}
