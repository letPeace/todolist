package com.todo.todo.controllers;

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
import com.todo.todo.services.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final String updatePage = "update_category";
    private final String createPage = "create_category";
    private final String redirectCategoriesPage = "redirect:/categories";

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("")
    public ModelAndView getCategoriesPage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("categories");
        modelAndView.addObject("categories", categoryService.findAll());
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public String getUpdateTaskForm(@PathVariable("id") Long id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return updatePage;
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid Category category, @AuthenticationPrincipal User user, BindingResult result){
        return categoryService.update(category, user, result) ? redirectCategoriesPage : "redirect:/categories/update/{id}";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        categoryService.delete(id);
        return redirectCategoriesPage;
    }

    @GetMapping("/create")
    public String getCreateCategoryForm(Category category){
        return createPage;
    }

    @PostMapping("/create")
    public String createCategory(@AuthenticationPrincipal User user, @Valid Category category, BindingResult result){
        return categoryService.create(category, user, result) ? redirectCategoriesPage : createPage;
    }

}
