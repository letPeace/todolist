package com.todo.todo.services;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Role;
import com.todo.todo.models.User;
import com.todo.todo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    private final String createPage = "create_user";
    private final String redirectLoginPage = "redirect:/login";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);
        if(userDetails == null) throw new UsernameNotFoundException("User not found by username = " + username);
        return userDetails;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public ModelAndView create(User user, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(redirectLoginPage);
        if(result.hasErrors()){
            modelAndView.setViewName(createPage);
            modelAndView.addObject("error", "User creation error!");
            return modelAndView;
        }
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if(userFromDatabase != null){
            modelAndView.setViewName(createPage);
            modelAndView.addObject("error", "User has been already registrated!");
            return modelAndView;
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedDate(Instant.now());
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return modelAndView;
    }

    public Boolean delete(User user, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        userRepository.delete(user);
        return Boolean.TRUE;
    }

}
