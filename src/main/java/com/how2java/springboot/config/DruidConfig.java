package com.how2java.springboot.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfig {
  private static Logger logger = Logger.getLogger(DruidConfig.class);

  @Bean
  public ServletRegistrationBean druidStatViewServlet() {
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    registrationBean.addInitParameter("loginUsername", "root");
    registrationBean.addInitParameter("loginPassword", "123456");
    //是否能够重置数据 禁用HTML页面上的“Reset All”功能
    registrationBean.addInitParameter("resetEnable", "false");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean druidWebStatViewFilter() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(new WebStatFilter());
    //设置过滤器过滤路径
    registrationBean.addInitParameter("urlPatterns", "/*");
    //忽略过滤的形式
    registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
    return registrationBean;
  }

//  @Bean
//  @ConfigurationProperties("spring.datasource")
//  public DataSource dataSourceOne(){
//    return DruidDataSourceBuilder.create().build();
//  }


//  @Bean
//  public DataSource druidDataSource(@Value("${spring.datasource.url}") String url,
//      @Value("${spring.datasource.driver-class-name}") String driver,
//      @Value("${spring.datasource.username}") String userName,
//      @Value("${spring.datasource.password}") String password,
//      @Value("${spring.datasource.maxActive}") int maxActive,
//      @Value("${spring.datasource.filters}") String filters,
//      @Value("${spring.datasource.initialSize}")
//          int initialSize,
//      @Value("${spring.datasource.minIdle}")
//          int minIdle,
//      @Value("${spring.datasource.maxWait}")
//          int maxWait,
//      @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//          int timeBetweenEvictionRunsMillis,
//      @Value("${spring.datasource.poolPreparedStatements}")
//          boolean poolPreparedStatements,
//      @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//          int maxPoolPreparedStatementPerConnectionSize,
//      @Value("${spring.datasource.connectionProperties}")
//          String connectionProperties) {
//    DruidDataSource dataSource = new DruidDataSource();
//    /*数据源主要配置*/
//    dataSource.setUrl(url);
//    dataSource.setDriverClassName(driver);
//    dataSource.setUsername(userName);
//    dataSource.setPassword(password);
//
//    /*数据源补充配置*/
//    dataSource.setMaxActive(maxActive);
//    dataSource.setInitialSize(initialSize);
//    dataSource.setMinIdle(minIdle);
//    dataSource.setMaxWait(maxWait);
//    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//    dataSource.setPoolPreparedStatements(poolPreparedStatements);
//    dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//    dataSource.setConnectionProperties(connectionProperties);
//    try {
//      dataSource.setFilters(filters);
//      logger.info("Druid数据源初始化设置成功......");
//    } catch (SQLException e) {
//      e.printStackTrace();
//      logger.info("Druid数据源filters设置失败......");
//    }
//    return dataSource;
//  }

}
