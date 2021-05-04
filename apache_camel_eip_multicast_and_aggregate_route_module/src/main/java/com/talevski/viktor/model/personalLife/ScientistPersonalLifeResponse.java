package com.talevski.viktor.model.personalLife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistPersonalLifeResponse {
    private ScientistPersonalLifeResult scientistPersonalLifeResult;
    private ScientistPersonalLifeError scientistPersonalLifeError;
}
