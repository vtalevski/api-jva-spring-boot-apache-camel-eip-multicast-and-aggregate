package com.talevski.viktor.configuration;

import org.hibernate.dialect.PostgreSQL94Dialect;

import static java.sql.Types.JAVA_OBJECT;

public class ScientistPersonalLifePostgreSQL94Dialect extends PostgreSQL94Dialect {
    public ScientistPersonalLifePostgreSQL94Dialect() {
        this.registerColumnType(JAVA_OBJECT, "jsonb");
    }
}
