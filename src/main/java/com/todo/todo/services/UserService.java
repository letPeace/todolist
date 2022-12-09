package com.todo.todo.services;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    /*
     * METHODS CALLING REPOSITORY
     */

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

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
    }

    /*
     * METHODS CALLED BY CONTROLLER
     */

    public ModelAndView create(User user, BindingResult result, ModelAndView model){
        if(result.hasErrors()){
            model.addObject("error", "User creation error!");
            return model;
        }
        User userFromDatabase = findByUsername(user.getUsername());
        if(userFromDatabase != null){
            model.addObject("error", "User has been already registrated!");
            return model;
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedDate(Instant.now());
        user.setRoles(Collections.singleton(Role.USER));
        return model;
    }

    public Boolean update(User user, User userForm, BindingResult result, Map<String, String> form, ModelAndView model){
        if(result.hasErrors()){
            model.addObject("error", result.getAllErrors().get(0).getDefaultMessage());
            return Boolean.FALSE;
        }
        String usernameStored = user.getUsername();
        String username = form.get("username");
        String usernameConfirm = form.get("usernameConfirm");
        String password = form.get("password");
        String passwordConfirm = form.get("passwordConfirm");
        if(!usernameStored.equals(username) && findByUsername(username)!=null){
            model.addObject("error", "User with such username already exists");
            return Boolean.FALSE;
        }
        if(!username.equals(usernameConfirm)){
            model.addObject("error", "Usernames must match");
            return Boolean.FALSE;
        }
        if(!password.equals(passwordConfirm)){
            model.addObject("error", "Passwords must match");
            return Boolean.FALSE;
        }
        return update(user, userForm, form);
    }

    public Boolean update(User user, User userForm, Map<String, String> form){
        user.setUsername(userForm.getUsername());
        String encodedPassword = new BCryptPasswordEncoder().encode(userForm.getPassword());
        user.setPassword(encodedPassword);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.clearRoles();
        for(String key : form.keySet()){
            if(roles.contains(key)) user.addRole(key);
        }

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
