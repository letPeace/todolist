package com.todo.todo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.repositories.TaskRepository;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/home")
    public ModelAndView getHomePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("tasks", taskRepository.findAll());
        return modelAndView;
    }

    @ResponseBody
    @GetMapping("/page")
    public String getPage(){
        return "page is here";
    }

}
