package com.how2java.springboot.datasource;

public class DynamicDataSourceContext {
  private static final ThreadLocal<DynamicDataSourceEnum> holder = new ThreadLocal<>();

  private DynamicDataSourceContext() {
    //
  }

  static void setDataSource(DynamicDataSourceEnum dataSource) {
    holder.set(dataSource);
  }

  static DynamicDataSourceEnum getDataSource() {
    return holder.get();
  }

  static void clearDataSource() {
    holder.remove();
  }

}
