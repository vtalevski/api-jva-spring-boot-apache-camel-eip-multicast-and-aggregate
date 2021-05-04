package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistPersonalLifeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.talevski.viktor.util.ScientistPersonalLifeConstants.EMPTY_SPACE;

@Service
@Slf4j
public class ScientistPersonalLifeServiceImpl implements ScientistPersonalLifeService {
    private ScientistPersonalLifeRepository scientistPersonalLifeRepository;

    public ScientistPersonalLifeServiceImpl(ScientistPersonalLifeRepository scientistPersonalLifeRepository) {
        this.scientistPersonalLifeRepository = scientistPersonalLifeRepository;
    }

    @Override
    public ScientistPersonalLifeResponse getScientistPersonalLife(ScientistRequest scientistRequest) {
        log.info("The getScientistPersonalLife method of the ScientistPersonalLifeServiceImpl class.");

        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());

        ScientistPersonalLifeResponse scientistPersonalLifeResponse = new ScientistPersonalLifeResponse();
        scientistPersonalLifeResponse.setScientistPersonalLifeResult(scientistPersonalLifeRepository.getOne(scientistFirstAndLastName).getScientistPersonalLifeResult());
        return scientistPersonalLifeResponse;
    }
}
