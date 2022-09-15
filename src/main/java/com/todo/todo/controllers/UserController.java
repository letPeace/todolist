package com.todo.todo.controllers;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Role;
import com.todo.todo.models.User;
import com.todo.todo.repositories.UserRepository;

@Controller
public class UserController {

    private final String registratePage = "registrate";
    private final String redirectLoginPage = "redirect:/login";
    private final String redirectUsersPage = "redirect:/users";

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ModelAndView getUsersPage(@AuthenticationPrincipal User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", userRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model){
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found by id = " + id));
        userRepository.delete(user);
        return redirectUsersPage;
    }
    
    @GetMapping("/registrate")
    public String getRegistratePage(){
        return registratePage;
    }

    @PostMapping("/registrate")
    public String registrateUser(@Valid User user, Map<String, Object> map){
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if(userFromDatabase != null){
            map.put("message", "User has been already registrated!");
            return registratePage;
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedDate(Instant.now());
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        map.put("message", "User has been successfully registrated!");
        return redirectLoginPage;
    }

}
