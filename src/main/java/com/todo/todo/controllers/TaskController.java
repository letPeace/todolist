package com.todo.todo.controllers;

import java.time.Instant;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Task;
import com.todo.todo.repositories.TaskRepository;

@Controller
public class TaskController {

    private final String updateTaskPage = "update_task";
    private final String createTaskPage = "create_task";
    private final String redirectHomePage = "redirect:/home";

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/home")
    public ModelAndView getHomePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("tasks", taskRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public String getUpdateTaskForm(@PathVariable("id") Long id, Model model){
        Task task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
        model.addAttribute("task", task);
        return updateTaskPage;
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid Task task, BindingResult result, Model model){
        if(result.hasErrors()){
            task.setId(id);
            return updateTaskPage;
        }
        task.setModifiedDate(Instant.now());
        taskRepository.save(task);
        return redirectHomePage;
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id, Model model){
        Task task = taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
        taskRepository.delete(task);
        return redirectHomePage;
    }

    @GetMapping("/create")
    public String getCreateTaskForm(Task task){
        return createTaskPage;
    }

    @PostMapping("/create")
    public String createTask(@Valid Task task, BindingResult result, Model model){
        if(result.hasErrors()){
            return createTaskPage;
        }
        task.setCompleted(Boolean.FALSE);
        task.setCreatedDate(Instant.now());
        task.setModifiedDate(Instant.now());
        taskRepository.save(task);
        return redirectHomePage;
    }

}
