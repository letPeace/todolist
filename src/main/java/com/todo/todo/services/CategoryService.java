package com.todo.todo.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.repositories.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    @Lazy
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    private TaskService taskService;

    public void save(Category category){
        categoryRepository.save(category);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public List<Category> findAllByUser(User user){
        return categoryRepository.findAllByUser(user.getId());
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Category not found by id = " + id));
    }

    public Boolean create(Category category, BindingResult result, User user){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        category.setCreatedDate(Instant.now());
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        return Boolean.TRUE;
    }

    public Boolean update(Category category, BindingResult result, User user){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        return Boolean.TRUE;
    }

    public Boolean delete(Category category, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        return delete(category);
    }

    public Boolean delete(Category category){
        User user = category.getUser();
        if(user != null){
            user.getCategories().remove(category);
            userService.save(user);
        }
        taskService.deleteAll(category.getTasks());
        categoryRepository.delete(category);
        return Boolean.TRUE;
    }

    public Boolean deleteAll(Set<Category> categories){
        Set<Category> categoriesCopy = new HashSet<>(categories);
        for(Category category : categoriesCopy){
            Boolean deletingSuccess = delete(category);
            if(!deletingSuccess) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
