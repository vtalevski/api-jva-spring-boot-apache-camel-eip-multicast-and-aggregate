package com.talevski.viktor.model.personalLife;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistPersonalLifeResult {
    private Integer birthYear;
    private Integer deathYear;
    private String birthCountry;
    private String deathCountry;
    private List<Wife> wives;
}
