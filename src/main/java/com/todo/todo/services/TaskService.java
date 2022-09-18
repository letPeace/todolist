package com.todo.todo.services;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task findById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
    }

    public Boolean create(Task task, User user, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        task.setCompleted(Boolean.FALSE);
        task.setCreatedDate(Instant.now());
        task.setModifiedDate(Instant.now());
        task.setUser(user);
        taskRepository.save(task);
        return Boolean.TRUE;
    }

    public Boolean update(Task task, User user, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        task.setModifiedDate(Instant.now());
        task.setUser(user);
        taskRepository.save(task);
        return Boolean.TRUE;
    }

    public Boolean delete(Long id){
        Task task = findById(id);
        taskRepository.delete(task);
        return Boolean.TRUE;
    }
    
}
