package com.talevski.viktor.configuration;

import org.hibernate.dialect.PostgreSQL94Dialect;

import static java.sql.Types.JAVA_OBJECT;

public class ScientistProfessionalLifePostgreSQL94Dialect extends PostgreSQL94Dialect {
    public ScientistProfessionalLifePostgreSQL94Dialect() {
        this.registerColumnType(JAVA_OBJECT, "jsonb");
    }
}
