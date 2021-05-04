package com.talevski.viktor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScientistError {
    private ScientistErrorCode errorCode;
    private String errorMessage;
}
