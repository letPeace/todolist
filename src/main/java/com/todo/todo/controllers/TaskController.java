package com.todo.todo.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
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

    @GetMapping("/update/{task}")
    public String getUpdateTaskPage(@Valid Task task, BindingResult result, Model model){
        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.findAll());
        return updatePage;
    }

    @PostMapping("/update/{task}")
    public String updateTask(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, @RequestParam Map<Object, Object> form){
        Category category = categoryService.findById(Long.parseLong(form.get("category").toString()));
        return taskService.update(task, result, user, form, category) ? redirectHomePage : updatePage;
    }

    @PostMapping("/delete/{task}")
    public String deleteTask(@Valid Task task, BindingResult result){
        taskService.delete(task, result);
        return redirectHomePage;
    }

    @GetMapping("/create")
    public String getCreateTaskPage(Task task, Model model){
        model.addAttribute("categories", categoryService.findAll());
        return createPage;
    }

    @PostMapping("/create")
    public String createTask(@Valid Task task, BindingResult result, @AuthenticationPrincipal User user, @RequestParam Map<Object, Object> form){
        Category category = categoryService.findById(Long.parseLong(form.get("category").toString()));
        return taskService.create(task, result, user, category) ? redirectHomePage : createPage;
    }

}
