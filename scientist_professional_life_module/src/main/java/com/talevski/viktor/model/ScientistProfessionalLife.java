package com.talevski.viktor.model;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "public", name = "scientist_professional_life")
@TypeDef(name = "ScientistProfessionalLifeResult", typeClass = ScientistProfessionalLifeUserType.class)
public class ScientistProfessionalLife {

    @Id
    private String scientistFirstAndLastName;

    @Type(type = "ScientistProfessionalLifeResult")
    private ScientistProfessionalLifeResult scientistProfessionalLifeResult;
}
