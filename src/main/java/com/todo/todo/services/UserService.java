package com.todo.todo.services;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    @Autowired
    private CategoryService categoryService;

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

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public ModelAndView create(User user, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(redirectLoginPage);
        if(result.hasErrors()){
            modelAndView.setViewName(createPage);
            modelAndView.addObject("error", "User creation error!");
            return modelAndView;
        }
        User userFromDatabase = findByUsername(user.getUsername());
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

    public Boolean update(User user, BindingResult result, Map<Object, Object> form){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        String usernameStored = user.getUsername();
        String username = (String) form.get("username");
        String usernameConfirm = (String) form.get("usernameConfirm");
        String password = (String) form.get("password");
        String passwordConfirm = (String) form.get("passwordConfirm");
        if(!username.equals(usernameConfirm) ||
            !password.equals(passwordConfirm) ||
            !usernameStored.equals(username) && findByUsername(username)!=null) return Boolean.FALSE;
        return update(user, form);
    }

    public Boolean update(User user, Map<Object, Object> form){
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for(Object key : form.keySet()){
            if(roles.contains(key)) user.getRoles().add(Role.valueOf((String) key));
        }

        userRepository.save(user);
        return Boolean.TRUE;
    }

    public Boolean delete(User user, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        return delete(user);
    }

    public Boolean delete(User user){
        categoryService.deleteAll(user.getCategories());
        userRepository.delete(user);
        return Boolean.TRUE;
    }

}
