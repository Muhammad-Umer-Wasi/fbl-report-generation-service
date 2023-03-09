package com.example.BPMservices;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

  // private String DEV_URL = "jdbc:oracle:thin:@172.20.3.247:1521/bpmdbinst";
  // private String DEV_USERNAME = "BPMFBLDB";
  // private String DEV_PASSWORD = "BPMFBLDB";

  // private String FBL_DEV_URL = "jdbc:oracle:thin:@10.14.0.71:1521/bpmdev";
  // private String FBL_DEV_USERNAME = "BPMFBLDB";
  // private String FBL_DEV_PASSWORD = "bpmfbldb_123";

  // private String FBL_UAT_URL = "jdbc:oracle:thin:@10.14.0.127:1521/bpmprodpdb";
  // private String FBL_UAT_USERNAME = "BPMFBLDB";
  // private String FBL_UAT_PASSWORD = "bpmfbldb_123";

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
