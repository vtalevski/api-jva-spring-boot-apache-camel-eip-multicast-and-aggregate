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
public class ScientistProfessionalLifeResult implements Serializable {
    private List<Education> educationList;
    private List<String> knownFor;
}
