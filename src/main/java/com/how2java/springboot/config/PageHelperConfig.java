package com.how2java.springboot.config;

import com.github.pagehelper.PageHelper;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageHelperConfig {
  @Bean
  public PageHelper pageHelper() {
    PageHelper pageHelper = new PageHelper();
    Properties p = new Properties();
    p.setProperty("helperDialect", "mysql");
    p.setProperty("auto-dialect", "true");
    p.setProperty("auto-runtime-dialect", "true");
    p.setProperty("offsetAsPageNum", "true");
    p.setProperty("rowBoundsWithCount", "true");
    p.setProperty("reasonable", "true");
    pageHelper.setProperties(p);
    return pageHelper;
  }
}
