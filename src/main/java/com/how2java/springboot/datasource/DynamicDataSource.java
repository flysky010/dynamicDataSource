package com.how2java.springboot.datasource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

  private Map<String, DataSource> dataSourceMap = new LinkedHashMap<>(2);

  @Override
  protected Object determineCurrentLookupKey() {
    return DynamicDataSourceContext.getDataSource().name();
  }

  @Override
  public void afterPropertiesSet() {
    if (this.dataSourceMap == null || this.dataSourceMap.size() == 0) {
      throw new IllegalArgumentException("动态数据源配置为空");
    }

    Map<Object, Object> targetDataSources = new HashMap<>(2);
    targetDataSources.putAll(dataSourceMap);
    setTargetDataSources(targetDataSources);
//    setDefaultTargetDataSource(dataSourceMap.get(DynamicDataSourceEnum.SHARD1.getKey()));
    super.afterPropertiesSet();
  }

  public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
    this.dataSourceMap = dataSourceMap;
  }

}
