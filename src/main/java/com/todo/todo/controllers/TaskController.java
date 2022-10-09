package com.todo.todo.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.models.singletons.CategorySingleton;
import com.todo.todo.services.CategoryService;
import com.todo.todo.services.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final String updatePage = "update_task";
    private final String createPage = "create_task";
    private final String redirectHomePage = "redirect:/users/home";

    @Autowired
    private TaskService taskService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public ModelAndView getCreateTaskPage(ModelAndView model){
        List<Category> categories = categoryService.findAll();
        Category categorySelected = categories.isEmpty() ? CategorySingleton.get() : categories.get(0);
        model.setViewName(createPage);
        model.addObject("categories", categoryService.findAll());
        model.addObject("categorySelected", categorySelected);
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createTask(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, @RequestParam Map<Object, Object> form, ModelAndView model){
        Category category = categoryService.findById(Long.parseLong(form.get("category").toString()));
        if(taskService.create(task, result, user, category)){
            model.setViewName(redirectHomePage);
            taskService.save(task);
        } else{
            model.setViewName(createPage);
            model.addObject("task", task);
            model.addObject("categories", categoryService.findAll());
            model.addObject("categorySelected", category);
        }
        return model;
    }
    
    @GetMapping("/update/{task}")
    public ModelAndView getUpdateTaskPage(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, ModelAndView model){
        if(!(user.equals(task.getUser()) || user.isAdmin())){
            model.setViewName(redirectHomePage);
            return model;
        }
        model.setViewName(updatePage);
        model.addObject("task", task);
        model.addObject("categories", categoryService.findAll());
        return model;
    }

    @PostMapping("/update/{task}")
    public ModelAndView updateTask(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, @RequestParam Map<String, String> form, ModelAndView model){
        if(!(user.equals(task.getUser()) || user.isAdmin())){
            model.setViewName(redirectHomePage);
            return model;
        }
        // Fields 'text' and 'category' are already filled with updated values
        if(taskService.update(task, result, form)){
            model.setViewName(redirectHomePage);
            taskService.save(task);
        } else{
            model.setViewName(updatePage);
            model.addObject("categories", categoryService.findAll());
            // leave task with changed text, but not in repository
        }
        return model;
    }

    @PostMapping("/delete/{task}")
    public ModelAndView deleteTask(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, ModelAndView model){
        model.setViewName(redirectHomePage);
        if(!(user.equals(task.getUser()) || user.isAdmin())) return model;
        taskService.delete(task, result);
        return model;
    }

}
