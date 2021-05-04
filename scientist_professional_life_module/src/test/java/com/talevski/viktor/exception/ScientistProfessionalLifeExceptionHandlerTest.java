package com.talevski.viktor.exception;

import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.util.TestJsonFilesPaths;
import com.talevski.viktor.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(value = MockitoExtension.class)
public class ScientistProfessionalLifeExceptionHandlerTest {

    @Mock
    private EntityNotFoundException entityNotFoundException;
    @InjectMocks
    private ScientistProfessionalLifeExceptionHandler scientistProfessionalLifeExceptionHandler;

    @Test
    public void shouldHandleEntityNotFoundException() throws IOException {
        ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse = TestUtil.readJsonFile(TestJsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_ERROR, ScientistProfessionalLifeResponse.class);
        ResponseEntity<ScientistProfessionalLifeResponse> expectedScientistProfessionalLifeResponseResponseEntity = ResponseEntity.ok(expectedScientistProfessionalLifeResponse);

        ResponseEntity<ScientistProfessionalLifeResponse> actualScientistProfessionalLifeResponseResponseEntity = scientistProfessionalLifeExceptionHandler.handleEntityNotFoundException(entityNotFoundException);

        assertEquals(expectedScientistProfessionalLifeResponseResponseEntity, actualScientistProfessionalLifeResponseResponseEntity);
    }
}