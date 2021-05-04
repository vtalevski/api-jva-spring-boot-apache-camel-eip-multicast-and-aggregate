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
public class Wife {
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private Integer deathYear;
    private String birthCountry;
    private String deathCountry;
    private List<Child> children;
}
