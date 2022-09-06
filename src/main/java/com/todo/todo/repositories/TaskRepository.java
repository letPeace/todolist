package com.todo.todo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.todo.todo.models.Task;

public interface TaskRepository extends CrudRepository<Task, Long>{
    
}
