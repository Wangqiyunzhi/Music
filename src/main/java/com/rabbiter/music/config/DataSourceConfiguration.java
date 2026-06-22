package com.rabbiter.music.config;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ExceptionSorter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Description
 * Author: rabbiter
 * Date: 2024/2/26 23:39
 **/
@Configuration
public class DataSourceConfiguration {
  @Value("${spring.datasource.driver-class-name}")
  private String jdbcDriver;
  @Value("${spring.datasource.url}")
  private String jdbcUrl;
  @Value("${spring.datasource.username}")
  private String jdbcUsername;
  @Value("${spring.datasource.password}")
  private String jdbcPassword;

  @Bean(name="dataSource")
  public DruidDataSource createDataSource() throws Exception {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(jdbcDriver);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(jdbcUsername);
    dataSource.setPassword(jdbcPassword);

    dataSource.setDefaultAutoCommit(false);
    dataSource.setBreakAfterAcquireFailure(true);
    dataSource.setFailFast(true);
    dataSource.setConnectionErrorRetryAttempts(0);
    dataSource.setExceptionSorter(new CustomExceptionSorter());

    return dataSource;
  }

}

class CustomExceptionSorter implements ExceptionSorter {

  @Override
  public boolean isExceptionFatal(SQLException e) {
    e.printStackTrace();
    return true;
  }

  @Override
  public void configFromProperties(Properties properties) {

  }
}


