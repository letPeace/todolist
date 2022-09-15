package com.todo.todo.controllers;

import java.time.Instant;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.repositories.CategoryRepository;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @GetMapping("")
    public ModelAndView getCategoriesPage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("categories");
        modelAndView.addObject("categories", categoryRepository.findAll());
        return modelAndView;
    }

    @GetMapping("update/{id}")
    public String getUpdateTaskForm(@PathVariable("id") Long id, Model model){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found by id = " + id));
        model.addAttribute("category", category);
        return "update_category";
    }

    @PostMapping("update/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid Category category, @AuthenticationPrincipal User user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "redirect:/categories/update/{id}";
        }
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("delete/{id}")
    public String deleteTask(@PathVariable("id") Long id, Model model){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
        categoryRepository.delete(category);
        return "redirect:/categories";
    }

    @GetMapping("create")
    public String getCreateCategoryForm(Category category){
        return "create_category";
    }

    @PostMapping("create")
    public String createCategory(@AuthenticationPrincipal User user, @Valid Category category, BindingResult result, Model model){
        if(result.hasErrors()){
            return "redirect:/categories/create";
        }
        category.setCreatedDate(Instant.now());
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

}
