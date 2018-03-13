package com.whimmy.revenue.db.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.whimmy.revenue.db.repo")
public class PersistenceConfig {


  @Bean(value = "entityManagerFactory")
  LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(
      @Qualifier(value = "dataSource") DataSource dataSource,
      @Qualifier(value = "hibernateProperties") Properties properties) {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(dataSource);
    factory.setJpaVendorAdapter(jpaVendorAdapter());
    factory.setPackagesToScan("com.whimmy.revenue.db.entity");
    factory.setJpaProperties(properties);
    return factory;
  }

  private HibernateJpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.H2);
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(true);
    return vendorAdapter;
  }

  @Bean
  @Qualifier(value = "dataSource")
  DataSource h2FileDataSource() {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setUrl("jdbc:h2:/D:/urcc;MODE=MySQL;AUTO_SERVER=TRUE");
    dataSource.setUser("");
    dataSource.setPassword("");
    return dataSource;
  }

  @Bean
  @Qualifier(value = "hibernateProperties")
  Properties hibernatePropertiesIT() {
    Properties properties = _hibernateProperties();
    properties.setProperty("hibernate.dialect", H2Dialect.class.getName());
    return properties;
  }

  private Properties _hibernateProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    // properties.setProperty("hibernate.hbm2ddl.auto", "update");
    properties.setProperty("hibernate.connection.useUnicode", "true");
    properties.setProperty("hibernate.connection.characterEncoding", "UTF-8");
    properties.setProperty("hibernate.connection.charSet", "UTF-8");
    return properties;
  }
}
