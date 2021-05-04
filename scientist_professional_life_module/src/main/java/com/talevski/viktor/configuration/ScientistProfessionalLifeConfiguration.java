package com.talevski.viktor.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.talevski.viktor.model.ScientistProfessionalLife;
import com.talevski.viktor.repository.ScientistProfessionalLifeRepository;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.talevski.viktor.util.MainJsonFilesPaths.SCIENTISTS;

@Configuration
public class ScientistProfessionalLifeConfiguration {
    private ScientistProfessionalLifeRepository scientistProfessionalLifeRepository;

    public ScientistProfessionalLifeConfiguration(ScientistProfessionalLifeRepository scientistProfessionalLifeRepository) {
        this.scientistProfessionalLifeRepository = scientistProfessionalLifeRepository;
    }

    @PostConstruct
    public void populateDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream(SCIENTISTS);
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, ScientistProfessionalLife.class);

        try {
            List<ScientistProfessionalLife> scientistProfessionalLifeList = objectMapper.readValue(inputStream, collectionType);
            scientistProfessionalLifeRepository.saveAll(scientistProfessionalLifeList);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
