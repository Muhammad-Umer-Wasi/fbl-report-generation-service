package com.example.BPMservices;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    dataSource.setUrl("jdbc:oracle:thin:@172.20.3.247:1521/bpmdbinst");
    dataSource.setUsername("BPMFBLDB");
    dataSource.setPassword("BPMFBLDB");
    return dataSource;
  }

  @Bean(name = "jdbcTemplate")
  public JdbcTemplate applicationDataConnection() {
    return new JdbcTemplate(dataSource());
  }
}
