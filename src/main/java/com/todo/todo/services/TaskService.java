package com.todo.todo.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Lazy
    @Autowired
    private UserService userService;

    public void save(Task task){
        taskRepository.save(task);
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public List<Task> findAllByUser(User user){
        return taskRepository.findAllByUser(user.getId());
    }

    public Task findById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Task not found by id = " + id));
    }

    public Boolean create(Task task, BindingResult result, User user, Category category){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        task.setCompleted(Boolean.FALSE);
        task.setCreatedDate(Instant.now());
        task.setModifiedDate(Instant.now());
        task.setUser(user);
        task.setCategory(category);
        return Boolean.TRUE;
    }

    public Boolean update(Task task, BindingResult result, Map<String, String> form){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        if(form.containsKey("completed")) task.setCompleted(Boolean.TRUE);
        else task.setCompleted(Boolean.FALSE);
        task.setModifiedDate(Instant.now());
        return Boolean.TRUE;
    }

    public Boolean delete(Task task, BindingResult result){
        if(result.hasErrors()){
            return Boolean.FALSE;
        }
        return delete(task);
    }

    public Boolean delete(Task task){
        Category category = task.getCategory();
        if(category != null){
            category.getTasks().remove(task);
            categoryService.save(category);
        }
        User user = task.getUser();
        if(user != null){
            user.getTasks().remove(task);
            userService.save(user);
        }
        taskRepository.delete(task);
        return Boolean.TRUE;
    }

    public Boolean deleteAll(Set<Task> tasks){
        Set<Task> tasksCopy = new HashSet<>(tasks);
        for(Task task : tasksCopy){
            Boolean deletingSuccess = delete(task);
            if(!deletingSuccess) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
}
