package com.how2java.springboot.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.how2java.springboot.datasource.DynamicDataSource;
import com.how2java.springboot.datasource.DynamicDataSourceEnum;
import com.how2java.springboot.datasource.DynamicDataSourceTransactionManager;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {

//  private final static String typeAliasPackage = "com.how2java.springboot.pojo";

  @Autowired
  @Qualifier("myBatisDynamicDataSourcePlugin")
  private Interceptor dataSourceInterceptor;


  @Value("${mybatis.mapper-locations}")
  private String mapperLocations;

  /**
   * master DataSource
   * @Primary 注解用于标识默认使用的 DataSource Bean，因为有2个 DataSource Bean，该注解可用于 master
   * 或 slave DataSource Bean, 但不能用于 dynamicDataSource Bean, 否则会产生循环调用
   *
   * @ConfigurationProperties 注解用于从 application.properties 文件中读取配置，为 Bean 设置属性
   * @return data source
   */
  @Bean("shard0")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.druid.shard0")
  public DataSource shard0() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean("shard1")
  @ConfigurationProperties(prefix = "spring.datasource.druid.shard1")
  public DataSource shard1() {
    return DruidDataSourceBuilder.create().build();
  }

  @Bean("SCMDataSource")
  public DynamicDataSource dynamicDataSource(){
    DynamicDataSource dynamicDataSource = new DynamicDataSource();
    Map<String, DataSource> dataSourceMap = new LinkedHashMap<>(2);
    dataSourceMap.put(DynamicDataSourceEnum.SHARD0.name(), shard0());//shard0创建对象，会被bean拦截，因此不会创建新的对象
    dataSourceMap.put(DynamicDataSourceEnum.SHARD1.name(), shard1());
    dynamicDataSource.setDataSourceMap(dataSourceMap);
    return dynamicDataSource;
  }

  /**
   * 配置 SqlSessionFactoryBean
   * @ConfigurationProperties 在这里是为了将 MyBatis 的 mapper 位置和持久层接口的别名设置到
   * Bean 的属性中，如果没有使用 *.xml 则可以不用该配置，否则将会产生 invalid bond statement 异常
   *
   * @return the sql session factory bean
   */
  @Bean
  public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

    // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
    sqlSessionFactoryBean.setDataSource(dynamicDataSource());

    // 加载MyBatis配置文件
    PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    // 能加载多个，所以可以配置通配符(如：classpath*:mapper/**/*.xml)
    sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocations));
//    sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);

    //加载MyBatis插件
    sqlSessionFactoryBean.setPlugins(new Interceptor[]{dataSourceInterceptor});

    return sqlSessionFactoryBean;
  }


  @Bean
  public PlatformTransactionManager transactionManager(@Qualifier("SCMDataSource") DynamicDataSource dataSource) throws Exception {
    return new DynamicDataSourceTransactionManager(dataSource);
  }

}
