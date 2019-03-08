package com.how2java.springboot.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.springboot.pojo.Category;
import com.how2java.springboot.service.CategoryService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

  private final static String key = "test";
  private final static String sessionId = "sessionId";

  @Autowired
  CategoryService categoryService;

  @GetMapping("/categories")
  public String listCategory(Model m,
      @RequestParam(value = "start", defaultValue = "0") int start,
      @RequestParam(value = "size", defaultValue = "5") int size,HttpServletRequest request) throws Exception {
    PageHelper.startPage(start,size,"id DESC");
    List<Category> cs=categoryService.findAll();
    PageInfo<Category> page = new PageInfo<>(cs);
    int value;
    if(request.getSession().getAttribute(key) == null) {
      value = 1;
    }else{
      value = (Integer) request.getSession().getAttribute(key);
    }
    m.addAttribute("page", page);
    m.addAttribute(key, value);
    m.addAttribute(sessionId, request.getSession().getId());
    return "listCategory";
  }

  @PostMapping("/categories")
  public String addCategory(Category c, HttpServletRequest request) throws Exception {
    int randomNum = RandomUtils.nextInt(1000000, 9999999);
    request.getSession().setAttribute(key, randomNum);
    categoryService.save(c);
    return "redirect:/categories";
  }

  @DeleteMapping("/categories/{id}")
  public String deleteCategory(Category c) throws Exception {
    categoryService.delete(c.getId());
    return "redirect:/categories";
  }

  @PutMapping("/categories/{id}")
  public String updateCategory(Category c) throws Exception {
    categoryService.update(c);
    return "redirect:/categories";
  }

  @GetMapping("/categories/{id}")
  public String getCategory(@PathVariable("id") int id, Model m) throws Exception {
    Category c= categoryService.get(id);
    m.addAttribute("c", c);
    return "editCategory";
  }
}
