package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.service.ScientistPersonalLifeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ScientistPersonalLifeControllerTest {

    @Mock
    private ScientistPersonalLifeService scientistPersonalLifeService;
    @InjectMocks
    private ScientistPersonalLifeController scientistPersonalLifeController;

    @Test
    public void shouldReturnScientistPersonalLifeResponseWithScientistPersonalLifeResult() throws IOException {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);
        ResponseEntity<ScientistPersonalLifeResponse> expectedScientistPersonalLifeResponseResponseEntity = ResponseEntity.ok(expectedScientistPersonalLifeResponse);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        when(scientistPersonalLifeService.getScientistPersonalLife(scientistRequest)).thenReturn(expectedScientistPersonalLifeResponse);
        ResponseEntity<ScientistPersonalLifeResponse> actualScientistPersonalLifeResponseResponseEntity = scientistPersonalLifeController.getScientistPersonalLife(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponseResponseEntity, actualScientistPersonalLifeResponseResponseEntity);
    }
}