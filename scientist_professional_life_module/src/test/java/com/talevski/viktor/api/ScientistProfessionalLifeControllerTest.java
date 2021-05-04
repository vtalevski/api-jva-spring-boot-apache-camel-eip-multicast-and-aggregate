package com.talevski.viktor.api;

import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.service.ScientistProfessionalLifeService;
import com.talevski.viktor.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ScientistProfessionalLifeControllerTest {

    @Mock
    private ScientistProfessionalLifeService scientistProfessionalLifeService;
    @InjectMocks
    private ScientistProfessionalLifeController scientistProfessionalLifeController;

    @Test
    public void shouldReturnScientistProfessionalLifeResponseWithScientistProfessionalLifeResult() throws IOException {
        ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse = TestUtil.readJsonFile(SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);
        ResponseEntity<ScientistProfessionalLifeResponse> expectedScientistProfessionalLifeResponseResponseEntity = ResponseEntity.ok(expectedScientistProfessionalLifeResponse);

        ScientistRequest scientistRequest = TestUtil.readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        when(scientistProfessionalLifeService.getScientistProfessionalLife(scientistRequest)).thenReturn(expectedScientistProfessionalLifeResponse);
        ResponseEntity<ScientistProfessionalLifeResponse> actualScientistProfessionalLifeResponseResponseEntity = scientistProfessionalLifeController.getScientistProfessionalLife(scientistRequest);

        assertEquals(expectedScientistProfessionalLifeResponseResponseEntity, actualScientistProfessionalLifeResponseResponseEntity);
    }
}