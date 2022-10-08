package com.todo.todo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final String redirectHomePage = "redirect:/users/home";

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public ModelAndView getCreateCategoryPage(ModelAndView model){
        model.setViewName(createPage);
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createCategory(@AuthenticationPrincipal User user, @Valid Category category, BindingResult result, ModelAndView model){
        if(categoryService.create(category, result, user)){
            model.setViewName(redirectHomePage);
            categoryService.save(category);
        } else{
            model.setViewName(createPage);
            model.addObject("category", category);
        }
        return model;
    }

    @GetMapping("/update/{category}")
    public ModelAndView getUpdateCategoryPage(@Valid Category category, BindingResult result, ModelAndView model){
        model.setViewName(updatePage);
        model.addObject("category", category);
        return model;
    }

    @PostMapping("/update/{category}")
    public ModelAndView updateCategory(@Valid Category category, BindingResult result, @AuthenticationPrincipal User user, ModelAndView model){
        if(categoryService.update(category, result, user)){
            model.setViewName(redirectHomePage);
            categoryService.save(category);
        } else{
            model.setViewName(updatePage);
        }
        return model;
    }

    @PostMapping("/delete/{category}")
    public String deleteCategory(@Valid Category category, BindingResult result){
        categoryService.delete(category, result);
        return redirectHomePage;
    }

}
