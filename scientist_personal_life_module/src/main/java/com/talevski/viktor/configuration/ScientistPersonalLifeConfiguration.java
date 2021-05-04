package com.talevski.viktor.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.talevski.viktor.model.ScientistPersonalLife;
import com.talevski.viktor.repository.ScientistPersonalLifeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.talevski.viktor.util.MainJsonFilesPaths.SCIENTISTS;

@Configuration
public class ScientistPersonalLifeConfiguration {
    private ScientistPersonalLifeRepository scientistPersonalLifeRepository;

    public ScientistPersonalLifeConfiguration(ScientistPersonalLifeRepository scientistPersonalLifeRepository) {
        this.scientistPersonalLifeRepository = scientistPersonalLifeRepository;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @PostConstruct
    public void populateDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream(SCIENTISTS);
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, ScientistPersonalLife.class);

        try {
            List<ScientistPersonalLife> scientistPersonalLifeList = objectMapper.readValue(inputStream, collectionType);
            scientistPersonalLifeRepository.saveAll(scientistPersonalLifeList);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
