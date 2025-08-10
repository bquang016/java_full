package com.example.art_gal.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        // Render tự động cung cấp biến môi trường DATABASE_URL
        String dbUrlString = System.getenv("DATABASE_URL");
        if (dbUrlString == null) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set.");
        }

        URI dbUri = new URI(dbUrlString);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        // Xây dựng lại URL theo định dạng JDBC
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}