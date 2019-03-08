package com.how2java.springboot.datasource;

import java.util.Properties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {
        MappedStatement.class,
        Object.class}),
    @Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class,
        Object.class,
        RowBounds.class,
        ResultHandler.class})})
public class MyBatisDynamicDataSourcePlugin implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {

    boolean activeTransaction = TransactionSynchronizationManager.isActualTransactionActive();
    if(!activeTransaction) {
      String dataSourceName;
      Object[] objects = invocation.getArgs();
      MappedStatement ms = (MappedStatement) objects[0];
      if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {//读操作，走shard0
        dataSourceName = DynamicDataSourceEnum.SHARD0.name();
      } else {
        dataSourceName = DynamicDataSourceEnum.SHARD1.name();
      }
      System.out.println("正在使用的数据源(shard0读，shard1写):" + dataSourceName);
      DynamicDataSourceContext.setDataSource(DynamicDataSourceEnum.valueOf(dataSourceName));
    }
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    if (target instanceof Executor) {
      return Plugin.wrap(target, this);
    }
    return target;
  }

  @Override
  public void setProperties(Properties properties) {

  }
}
