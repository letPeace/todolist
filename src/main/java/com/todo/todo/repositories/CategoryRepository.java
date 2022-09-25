package com.todo.todo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.todo.todo.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    @Query("FROM Category c WHERE c.user.id = :userId")
    List<Category> findAllByUser(@Param("userId") Long userId);

}
