package com.todo.todo.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Role;
import com.todo.todo.models.User;
import com.todo.todo.services.CategoryService;
import com.todo.todo.services.TaskService;
import com.todo.todo.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final String createPage = "create_user";
    private final String updatePage = "update_user";
    private final String redirectUsersPage = "redirect:/users/home";
    private final String redirectLoginPage = "redirect:/login";

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/home")//
    public ModelAndView getHomePage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("categories", categoryService.findAllByUser(user));
        modelAndView.addObject("tasks", taskService.findAllByUser(user));
        return modelAndView;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView getAdminPage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", userService.findAll());
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.addObject("tasks", taskService.findAll());
        return modelAndView;
    }
    
    @GetMapping("/create")
    public ModelAndView getCreateUserPage(ModelAndView model){
        model.setViewName(createPage);
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createUser(@Valid User user, BindingResult result, ModelAndView model){
        userService.create(user, result, model);
        Boolean hasError = model.getModelMap().containsKey("error");
        if(!hasError){
            model.setViewName(redirectLoginPage);
            userService.save(user);
        } else{
            model.setViewName(createPage);
            model.addObject("user", user);
        }
        return model;
    }

    @GetMapping("/update/{user}")
    public ModelAndView getUpdateTaskPage(@Valid User user, BindingResult result, ModelAndView model){
        if(result.hasErrors()){
            model.setViewName(redirectUsersPage);
            return model;
        }
        model.setViewName(updatePage);
        model.addObject("user", user);
        model.addObject("roles", Role.values());
        model.addObject("isAdmin", user.isAdmin());
        return model;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTask(@Valid User userForm, BindingResult result, @PathVariable(value="id") Long id, @RequestParam Map<String, String> form, ModelAndView model){
        User user = userService.findById(id);
        if(userService.update(user, userForm, result, form, model)){
            model.setViewName(redirectUsersPage);
            userService.save(user);
        } else{
            model.setViewName(updatePage);
        }
        return model;
    }

    @PostMapping("/delete/{user}")
    public String deleteUser(@Valid User user, BindingResult result, @AuthenticationPrincipal User userAuthenticated){
        userService.delete(user, result);
        if(user.equals(userAuthenticated)){
            // logout by POST request
        }
        return redirectUsersPage;
    }

}
