package com.how2java.springboot.test;

import com.how2java.springboot.utils.RedisUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;

public class RedisTest extends how2javaApplicationTest{

  @Autowired
  RedisUtil redisUtil;
  @Test
  public void Test(){
    redisUtil.set("name", "how2java");
    Assert.assertTrue(StringUtils.equals("how2java",redisUtil.get("name").toString()));
  }

  @Test
  public void Test2(){
    Map<String,Object> map=new HashMap<>();
    map.put("name", "meepo");
    map.put("pwd", "password");
    redisUtil.hmset("user", map);
    Assert.assertTrue(StringUtils.equals("meepo",redisUtil.hget("user","name").toString()));
  }
}
