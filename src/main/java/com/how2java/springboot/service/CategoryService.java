package com.how2java.springboot.service;

import com.how2java.springboot.mapper.CategoryMapper;
import com.how2java.springboot.pojo.Category;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

  @Autowired
  private CategoryMapper categoryMapper;

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public List<Category> findAll(){
    return categoryMapper.findAll();
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  public int save(Category category){
    return categoryMapper.save(category);
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  public void delete(int id){
    if(get(id) != null) {
      categoryMapper.delete(id);
    }
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  public Category get(int id){
    return categoryMapper.get(id);
  }

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  public int update(Category category){
    return categoryMapper.update(category);
  }

}
