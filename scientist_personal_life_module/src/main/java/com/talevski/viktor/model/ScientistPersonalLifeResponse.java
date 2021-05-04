package com.talevski.viktor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistPersonalLifeResponse implements Serializable {
    private ScientistPersonalLifeResult scientistPersonalLifeResult;
    private ScientistPersonalLifeError scientistPersonalLifeError;
}
