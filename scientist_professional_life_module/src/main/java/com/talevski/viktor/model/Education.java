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
public class Education implements Serializable {
    private String universityName;
    private Integer graduationYear;
}
