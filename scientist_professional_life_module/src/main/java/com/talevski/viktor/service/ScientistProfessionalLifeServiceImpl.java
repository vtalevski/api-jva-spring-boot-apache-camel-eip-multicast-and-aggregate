package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;
import com.talevski.viktor.repository.ScientistProfessionalLifeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.talevski.viktor.util.ScientistProfessionalLifeConstants.EMPTY_SPACE;

@Service
@Slf4j
public class ScientistProfessionalLifeServiceImpl implements ScientistProfessionalLifeService {
    private ScientistProfessionalLifeRepository scientistProfessionalLifeRepository;

    public ScientistProfessionalLifeServiceImpl(ScientistProfessionalLifeRepository scientistProfessionalLifeRepository) {
        this.scientistProfessionalLifeRepository = scientistProfessionalLifeRepository;
    }

    @Override
    public ScientistProfessionalLifeResponse getScientistProfessionalLife(ScientistRequest scientistRequest) {
        log.info("The getScientistProfessionalLife method of the ScientistProfessionalLifeServiceImpl class.");

        String scientistFirstAndLastName = scientistRequest.getFirstName().concat(EMPTY_SPACE).concat(scientistRequest.getLastName());

        ScientistProfessionalLifeResponse scientistProfessionalLifeResponse = new ScientistProfessionalLifeResponse();
        scientistProfessionalLifeResponse.setScientistProfessionalLifeResult(scientistProfessionalLifeRepository.getOne(scientistFirstAndLastName).getScientistProfessionalLifeResult());
        return scientistProfessionalLifeResponse;
    }
}
