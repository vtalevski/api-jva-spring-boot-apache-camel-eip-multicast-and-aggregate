package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistPersonalLife;
import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistPersonalLifeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.talevski.viktor.util.ScientistPersonalLifeConstants.EMPTY_SPACE;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT;
import static com.talevski.viktor.util.TestJsonFilesPaths.SCIENTIST_REQUEST_VALID;
import static com.talevski.viktor.util.TestUtil.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class ScientistPersonalLifeServiceImplTest {

    @Mock
    private ScientistPersonalLifeRepository scientistPersonalLifeRepository;
    @InjectMocks
    private ScientistPersonalLifeServiceImpl scientistPersonalLifeServiceImpl;

    @Test
    public void shouldReturnScientistResponse() throws IOException {
        ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse = readJsonFile(SCIENTIST_PERSONAL_LIFE_RESPONSE_WITH_SCIENTIST_PERSONAL_LIFE_RESULT, ScientistPersonalLifeResponse.class);

        ScientistRequest scientistRequest = readJsonFile(SCIENTIST_REQUEST_VALID, ScientistRequest.class);
        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());
        ScientistPersonalLife expectedScientistPersonalLife = createExpectedScientistPersonalLife(scientistFirstAndLastName, expectedScientistPersonalLifeResponse);
        when(scientistPersonalLifeRepository.getOne(scientistFirstAndLastName)).thenReturn(expectedScientistPersonalLife);
        ScientistPersonalLifeResponse actualScientistPersonalLifeResponse = scientistPersonalLifeServiceImpl.getScientistPersonalLife(scientistRequest);

        assertEquals(expectedScientistPersonalLifeResponse, actualScientistPersonalLifeResponse);
    }

    private ScientistPersonalLife createExpectedScientistPersonalLife(String scientistFirstAndLastName, ScientistPersonalLifeResponse expectedScientistPersonalLifeResponse) {
        ScientistPersonalLife scientistPersonalLife = new ScientistPersonalLife();
        scientistPersonalLife.setScientistFirstAndLastName(scientistFirstAndLastName);
        scientistPersonalLife.setScientistPersonalLifeResult(expectedScientistPersonalLifeResponse.getScientistPersonalLifeResult());
        return scientistPersonalLife;
    }
}