package com.talevski.viktor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Wife implements Serializable {
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private Integer deathYear;
    private String birthCountry;
    private String deathCountry;
    private List<Child> children;
}
