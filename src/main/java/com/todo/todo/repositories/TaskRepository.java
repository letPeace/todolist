package com.todo.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todo.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{
    
}
