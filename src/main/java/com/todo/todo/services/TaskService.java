package com.todo.todo.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.TaskRepository;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.validators.CategoryValidator;
import com.todo.todo.utils.validators.Validator;

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

    @Autowired
    @Qualifier("TaskValidator")
    private Validator taskValidator;
    @Autowired
    @Qualifier("CategoryValidator")
    private Validator categoryValidator;

    @Autowired
    private ApplicationContext context;

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
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundByIdException("Task not found by id = " + id));
    }

    public DataStorage create(User user, Map<String, String> form) throws RuntimeException{
        DataStorage storage = (DataStorage) context.getBean(DataStorage.class);
        // validate a text
        DataStorage storageText = taskValidator.getString(form, "text");
        storage.fill(storageText);
        String text = null;
        if(!storageText.hasExceptions()) text = (String) storageText.getData("text");
        // validate a category
        var categoryValidatorVar = (CategoryValidator) categoryValidator; // FIX IT
        DataStorage storageCategory = categoryValidatorVar.getCategory(form);
        storage.fill(storageCategory);
        Category category = null;
        if(!storageCategory.hasExceptions()) category = (Category) storageCategory.getData("category");
        // check if storage has exceptions
        if(storage.hasExceptions()) return storage;
        // create a new task
        Task task = new Task(); // DI must be here
        task.setText(text);
        task.setCompleted(Boolean.FALSE);
        task.setCreatedDate(Instant.now());
        task.setModifiedDate(Instant.now());
        task.setUser(user);
        task.setCategory(category);
        // save the task
        taskRepository.save(task);
        return storage;
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
