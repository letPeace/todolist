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
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.services.TaskService;

@Controller
public class TaskController {

    private final String updateTaskPage = "update_task";
    private final String createTaskPage = "create_task";
    private final String redirectHomePage = "redirect:/tasks";

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ModelAndView getTasksPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tasks");
        modelAndView.addObject("tasks", taskService.findAll());
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public String getUpdateTaskForm(@PathVariable("id") Long id, Model model){
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        return updateTaskPage;
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid Task task, @AuthenticationPrincipal User user, BindingResult result){
        return taskService.update(task, user, result) ? redirectHomePage : updateTaskPage; 
    }

    @GetMapping("/delete/{id}") // why GET ?
    public String deleteTask(@PathVariable("id") Long id, Model model){
        taskService.delete(id);
        return redirectHomePage;
    }

    @GetMapping("/create")
    public String getCreateTaskForm(Task task){
        return createTaskPage;
    }

    @PostMapping("/create")
    public String createTask(@AuthenticationPrincipal User user, @Valid Task task, BindingResult result){
        return taskService.create(task, user, result) ? redirectHomePage : createTaskPage;
    }

}
