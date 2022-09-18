package com.todo.todo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.User;
import com.todo.todo.services.UserService;

@Controller
public class UserController {

    private final String registratePage = "registrate";
    private final String redirectUsersPage = "redirect:/users";

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ModelAndView getUsersPage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.delete(id);
        return redirectUsersPage;
    }
    
    @GetMapping("/registrate")
    public String getRegistratePage(){
        return registratePage;
    }

    @PostMapping("/registrate")
    public ModelAndView registrateUser(@Valid User user){
        return userService.registrate(user);
    }

}
