package com.talevski.viktor.service;

import com.talevski.viktor.model.ScientistProfessionalLifeResponse;
import com.talevski.viktor.model.ScientistRequest;

public interface ScientistProfessionalLifeService {
    ScientistProfessionalLifeResponse getScientistProfessionalLife(ScientistRequest scientistRequest);
}
