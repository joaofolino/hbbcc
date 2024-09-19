package com.example.hbbcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"com.example.hbbcc"})
public class HbbccApplication {

    @Bean
    DataSourceInitializer initializer(DataSource dataSource) {

        var initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        var schema = new ClassPathResource("schema.sql");
        var discounts = new ClassPathResource("discounts.sql");
        var watches = new ClassPathResource("watches.sql");
        var populator = new ResourceDatabasePopulator(schema, discounts, watches);
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(HbbccApplication.class, args);
    }

}
