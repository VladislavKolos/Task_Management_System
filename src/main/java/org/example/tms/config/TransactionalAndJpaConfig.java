package org.example.tms.config;

import lombok.RequiredArgsConstructor;
import org.example.tms.properties.DataSourceProperties;
import org.example.tms.properties.HibernateProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({DataSourceProperties.class, HibernateProperties.class})
public class TransactionalAndJpaConfig {
    private final HibernateProperties hibernateProperties;
    private final DataSourceProperties dataSourceProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("org.example.tms.model");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(hibernateProperties());

        return factoryBean;
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        if (dataSourceProperties.getDriverClassName() != null) {
            dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        }

        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        return hibernateProperties.toProperties();
    }
}
