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
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.User;
import com.todo.todo.services.CategoryService;
import com.todo.todo.services.TaskService;
import com.todo.todo.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final String createPage = "create_user";
    private final String updatePage = "update_user";
    private final String redirectUsersPage = "redirect:/users";

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public ModelAndView getUsersPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("categories", categoryService.findAllByUser(user));
        modelAndView.addObject("tasks", taskService.findAllByUser(user));
        return modelAndView;
    }

    @PostMapping("/delete/{user}")
    public String deleteUser(@Valid User user, BindingResult result, @AuthenticationPrincipal User userAuthenticated){
        userService.delete(user, result);
        if(user.equals(userAuthenticated)){
            // logout by POST request
        }
        return redirectUsersPage;
    }
    
    @GetMapping("/create")
    public String getCreateUserPage(){
        return createPage;
    }

    @PostMapping("/create")
    public ModelAndView createUser(@Valid User user, BindingResult result){
        return userService.create(user, result);
    }

    @GetMapping("/update/{user}")
    public String getUpdateTaskPage(@Valid User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        return updatePage;
    }

    @PostMapping("/update/{user}")
    public String updateTask(@Valid User user, BindingResult result, @RequestParam Map<Object, Object> form){
        return userService.update(user, result, form) ? redirectUsersPage : updatePage;
    }

}
