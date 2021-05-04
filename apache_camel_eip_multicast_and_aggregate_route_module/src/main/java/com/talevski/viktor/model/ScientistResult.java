package com.talevski.viktor.model;

import com.talevski.viktor.model.personalLife.Wife;
import com.talevski.viktor.model.professionalLife.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistResult {
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private Integer deathYear;
    private String birthCountry;
    private String deathCountry;
    private List<Wife> wives;
    private List<Education> educationList;
    private List<String> knownFor;
}
