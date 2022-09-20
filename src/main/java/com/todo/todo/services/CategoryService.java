package com.todo.todo.services;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.repositories.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
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
        categoryRepository.save(category);
        return Boolean.TRUE;
    }

    public Boolean update(Category category, BindingResult result, User user){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        categoryRepository.save(category);
        return Boolean.TRUE;
    }

    public Boolean delete(Category category, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        categoryRepository.delete(category);
        return Boolean.TRUE;
    }

}
