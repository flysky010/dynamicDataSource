package com.how2java.springboot.datasource;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;


public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

  public DynamicDataSourceTransactionManager(DataSource dataSource) {
    super(dataSource);
  }

  /**
   * 只读事务到读库，读写事务到写库
   */
  @Override
  protected void doBegin(Object transaction, TransactionDefinition definition) {
    boolean readOnly = definition.isReadOnly();
    if (readOnly) {
      System.out.println("开启读事务");
      DynamicDataSourceContext.setDataSource(DynamicDataSourceEnum.SHARD0);
    } else {
      System.out.println("开启读写事务");
      DynamicDataSourceContext.setDataSource(DynamicDataSourceEnum.SHARD1);
    }
    super.doBegin(transaction, definition);
  }

  /**
   * 清理本地线程的数据源
   */
  @Override
  protected void doCleanupAfterCompletion(Object transaction) {
    super.doCleanupAfterCompletion(transaction);
    DynamicDataSourceContext.clearDataSource();
  }
}
