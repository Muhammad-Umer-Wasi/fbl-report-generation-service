package com.example.BPMservices;

import java.util.Base64;

import javax.sql.DataSource;

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
  // private String DEV_ENCODED_PASSWORD = "QlBNRkJMREI=";
  // private byte[] DEV_PASSWORD_BYTE =
  // Base64.getDecoder().decode(DEV_ENCODED_PASSWORD);
  // private String DEV_PASSWORD = new String(DEV_PASSWORD_BYTE);

   private String FBL_DEV_URL = "jdbc:oracle:thin:@10.42.42.161:1521/PBPMDEV";
  private String FBL_DEV_USERNAME = "BPMFBLDB";
  private String FBL_DEV_ENCODED_PASSWORD = "YnBtZmJsZGJfMTIz";
  private byte[] FBL_DEV_PASSWORD_BYTE =
  Base64.getDecoder().decode(FBL_DEV_ENCODED_PASSWORD);
  private String FBL_DEV_PASSWORD = new String(FBL_DEV_PASSWORD_BYTE);

  // private String FBL_UAT_URL = "jdbc:oracle:thin:@10.42.42.164:1521/PBPMUATNEW";
  // private String FBL_UAT_USERNAME = "BPMFBLDB";
  // private String FBL_UAT_ENCODED_PASSWORD = "YnBtZmJsZGJfMTIz";
  // private byte[] FBL_UAT_PASSWORD_BYTE =
  // Base64.getDecoder().decode(FBL_UAT_ENCODED_PASSWORD);
  // private String FBL_UAT_PASSWORD = new String(FBL_UAT_PASSWORD_BYTE);

  // private String FBL_PROD_URL = "jdbc:oracle:thin:@10.0.11.65:1521/bpmprodpdb";
  // private String FBL_PROD_USERNAME = "BPMFBLDB";
  // private String FBL_PROD_ENCODED_PASSWORD = "QlBNRkJMREJfNzg2";
  // private byte[] FBL_PROD_PASSWORD_BYTE =
  // Base64.getDecoder().decode(FBL_PROD_ENCODED_PASSWORD);
  // private String FBL_PROD_PASSWORD = new String(FBL_PROD_PASSWORD_BYTE);
  

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    dataSource.setUrl(FBL_DEV_URL);
    dataSource.setUsername(FBL_DEV_USERNAME);
    dataSource.setPassword(FBL_DEV_PASSWORD);
    return dataSource;
  }

  @Bean(name = "jdbcTemplate")
  public JdbcTemplate applicationDataConnection() {
    return new JdbcTemplate(dataSource());
  }
}
