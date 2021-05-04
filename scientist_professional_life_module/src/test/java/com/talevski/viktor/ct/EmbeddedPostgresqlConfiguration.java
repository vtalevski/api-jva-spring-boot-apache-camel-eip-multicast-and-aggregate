package com.talevski.viktor.ct;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

@Profile(value = "ct")
@Configuration
public class EmbeddedPostgresqlConfiguration {
    private Integer embeddedPostgresqlPort;

    public EmbeddedPostgresqlConfiguration(@Value("${embedded.postgresql.port}") Integer embeddedPostgresqlPort) {
        this.embeddedPostgresqlPort = embeddedPostgresqlPort;
    }

    @Bean
    public DataSource getDataSource() throws IOException {
        return EmbeddedPostgres.builder().setPort(embeddedPostgresqlPort).start().getPostgresDatabase();
    }
}
