package com.example.hbbcc.data;

import com.example.hbbcc.model.Product;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.event.BeforeConvertEvent;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableJdbcRepositories
public class CatalogConfiguration extends AbstractJdbcConfiguration {

    final AtomicInteger id = new AtomicInteger(0);

    @Bean
    public ApplicationListener<?> idSetting() {

        return (ApplicationListener<BeforeConvertEvent>) event -> {

            if (event.getEntity() instanceof Product) {
                setIds((Product) event.getEntity());
            }
        };
    }

    private void setIds(Product product) {

        if (product.getProductId() == 0) {
            product.setProductId(id.incrementAndGet());
        }

        product.getDiscounts().values().forEach(discount -> discount.setProductId(product.getProductId()));
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcOperations operations) {
        return new NamedParameterJdbcTemplate(operations);
    }

}
