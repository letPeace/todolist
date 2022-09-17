package com.todo.todo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.todo.todo.models.Task;
import com.todo.todo.repositories.TaskRepository;

public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAll(){
        return taskRepository.findAll();
    }
    
}
