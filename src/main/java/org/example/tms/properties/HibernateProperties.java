package org.example.tms.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Data
@ConfigurationProperties(prefix = "spring.jpa")
public class HibernateProperties {

    @Value("${spring.jpa.properties.hibernate.dialect}")
    @NotBlank
    private String hibernateDialect;

    @Value("${spring.jpa.show-sql}")
    @NotNull
    private Boolean showSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    @NotBlank
    private String ddlAuto;

    public Properties toProperties() {
        var properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", Boolean.toString(showSql));
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        return properties;
    }
}

