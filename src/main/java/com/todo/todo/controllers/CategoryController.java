package com.todo.todo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.services.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final String updatePage = "update_category";
    private final String createPage = "create_category";
    private final String redirectHomePage = "redirect:/users/home";

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/update/{category}")
    public String getUpdateCategoryPage(@Valid Category category, BindingResult result, Model model){
        model.addAttribute("category", category);
        return updatePage;
    }

    @PostMapping("/update/{category}")
    public String updateCategory(@Valid Category category, BindingResult result, @AuthenticationPrincipal User user){
        return categoryService.update(category, result, user) ? redirectHomePage : "redirect:/categories/update/{id}";
    }

    @PostMapping("/delete/{category}")
    public String deleteCategory(@Valid Category category, BindingResult result){
        categoryService.delete(category, result);
        return redirectHomePage;
    }

    @GetMapping("/create")
    public String getCreateCategoryPage(Category category){
        return createPage;
    }

    @PostMapping("/create")
    public String createCategory(@AuthenticationPrincipal User user, @Valid Category category, BindingResult result){
        return categoryService.create(category, result, user) ? redirectHomePage : createPage;
    }

}
