package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistPersonalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;

public interface ScientistPersonalLifeService {
    ScientistPersonalLifeResponse getScientistPersonalLife(ScientistRequest scientistRequest);
}
