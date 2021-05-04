package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR;
import static com.talevski.viktor.util.TestUtil.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(value = MockitoExtension.class)
public class ScientistPersonalLifeExceptionHandlerTest {

    @Mock
    private EntityNotFoundException entityNotFoundException;
    @InjectMocks
    private ScientistPersonalLifeExceptionHandler scientistPersonalLifeExceptionHandler;

    @Test
    public void shouldHandleEntityNotFoundException() throws IOException {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_ERROR, ScientistPersonalLifeResponse.class);
        ResponseEntity<ScientistPersonalLifeResponse> expectedScientistPersonalLifeResponseResponseEntity = ResponseEntity.ok(expectedScientistPersonalLifeResponse);

        ResponseEntity<ScientistPersonalLifeResponse> actualScientistPersonalLifeResponseResponseEntity = scientistPersonalLifeExceptionHandler.handleEntityNotFoundException(entityNotFoundException);

        assertEquals(expectedScientistPersonalLifeResponseResponseEntity, actualScientistPersonalLifeResponseResponseEntity);
    }
}