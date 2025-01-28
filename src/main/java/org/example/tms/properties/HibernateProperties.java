package org.example.tms.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Data
@ConfigurationProperties(prefix = "spring.jpa")
public class HibernateProperties {

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.jpa.show-sql}")
    private Boolean showSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", Boolean.toString(showSql));
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        return properties;
    }
}

