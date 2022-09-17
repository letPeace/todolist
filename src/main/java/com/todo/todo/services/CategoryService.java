package com.todo.todo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.todo.todo.models.Category;
import com.todo.todo.repositories.CategoryRepository;

public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

}
