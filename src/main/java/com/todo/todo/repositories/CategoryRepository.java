package com.todo.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todo.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
}
