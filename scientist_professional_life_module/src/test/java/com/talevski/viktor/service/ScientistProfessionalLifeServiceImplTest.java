package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistProfessionalLife;
import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistProfessionalLifeRepository;
import com.talevski.viktor.util.TestJsonFilesPaths;
import com.talevski.viktor.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.talevski.viktor.util.ScientistProfessionalLifeConstants.EMPTY_SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ScientistProfessionalLifeServiceImplTest {

    @Mock
    private ScientistProfessionalLifeRepository scientistProfessionalLifeRepository;
    @InjectMocks
    private ScientistProfessionalLifeServiceImpl scientistProfessionalLifeServiceImpl;

    @Test
    public void shouldReturnScientistResponse() throws IOException {
        ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse = TestUtil.readJsonFile(TestJsonFilesPaths.SCIENTIST_PROFESSIONAL_LIFE_RESPONSE_WITH_SCIENTIST_PROFESSIONAL_LIFE_RESULT, ScientistProfessionalLifeResponse.class);

        ScientistRequest scientistRequest = TestUtil.readJsonFile(TestJsonFilesPaths.SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        ScientistProfessionalLife expectedScientistProfessionalLife = createExpectedScientistProfessionalLife(scientistFirstAndLastName, expectedScientistProfessionalLifeResponse);
        when(scientistProfessionalLifeRepository.getOne(scientistFirstAndLastName)).thenReturn(expectedScientistProfessionalLife);
        ScientistProfessionalLifeResponse actualScientistProfessionalLifeResponse = scientistProfessionalLifeServiceImpl.getScientistProfessionalLife(scientistRequest);

        assertEquals(expectedScientistProfessionalLifeResponse, actualScientistProfessionalLifeResponse);
    }

    private ScientistProfessionalLife createExpectedScientistProfessionalLife(String scientistFirstAndLastName, ScientistProfessionalLifeResponse expectedScientistProfessionalLifeResponse) {
        ScientistProfessionalLife scientistProfessionalLife = new ScientistProfessionalLife();
        scientistProfessionalLife.setScientistFirstAndLastName(scientistFirstAndLastName);
        scientistProfessionalLife.setScientistProfessionalLifeResult(expectedScientistProfessionalLifeResponse.getScientistProfessionalLifeResult());
        return scientistProfessionalLife;
    }
}