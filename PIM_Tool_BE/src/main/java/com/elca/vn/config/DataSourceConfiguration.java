package com.elca.vn.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.elca.vn.config.DataSourceConfiguration.BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER;
import static com.elca.vn.config.DataSourceConfiguration.BEAN_MSSQL_PIM_SCHEMA_TRANSACTION_MANAGER;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.elca.vn.entity", "com.elca.vn.repository"},
        entityManagerFactoryRef = BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER,
        transactionManagerRef = BEAN_MSSQL_PIM_SCHEMA_TRANSACTION_MANAGER
)
public class DataSourceConfiguration {

    public static final String[] BASE_PACKAGE = {"com.elca.vn.entity", "com.elca.vn.repository"};
    public static final String BEAN_MSSQL_PIM_SCHEMA_DATASOURCE = "BEAN_MSSQL_PIM_SCHEMA_DATASOURCE";
    public static final String BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER = "BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER";
    public static final String BEAN_MSSQL_PIM_SCHEMA_TRANSACTION_MANAGER = "BEAN_MSSQL_PIM_SCHEMA_TRANSACTION_MANAGER";

    @Autowired
    private Environment env;

    @Primary
    @Bean(BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER)
    public LocalContainerEntityManagerFactoryBean pimSchemaEntityManager(@Qualifier(BEAN_MSSQL_PIM_SCHEMA_DATASOURCE) DataSource datasource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(datasource);
        em.setPackagesToScan(BASE_PACKAGE);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap();
        properties.put("hibernate.hbm2ddl.auto", this.env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", this.env.getProperty("spring.jpa.properties.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(BEAN_MSSQL_PIM_SCHEMA_DATASOURCE)
    @ConfigurationProperties(prefix = "mssql.pimschema.datasource.hikari")
    public DataSource pimSchemeDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(BEAN_MSSQL_PIM_SCHEMA_TRANSACTION_MANAGER)
    public PlatformTransactionManager pimSchemaTransactionManager(@Qualifier(BEAN_MSSQL_PIM_SCHEMA_ENTITY_MANAGER) LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
