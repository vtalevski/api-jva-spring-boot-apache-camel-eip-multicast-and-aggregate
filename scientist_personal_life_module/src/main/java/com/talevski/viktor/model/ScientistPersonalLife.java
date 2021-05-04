package com.talevski.viktor.model;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "public", name = "scientist_personal_life")
@TypeDef(name = "ScientistPersonalLifeResult", typeClass = ScientistPersonalLifeUserType.class)
public class ScientistPersonalLife {

    @Id
    private String scientistFirstAndLastName;

    @Type(type = "ScientistPersonalLifeResult")
    private ScientistPersonalLifeResult scientistPersonalLifeResult;
}
