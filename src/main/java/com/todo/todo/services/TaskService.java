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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.TaskRepository;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.validators.CategoryValidator;
import com.todo.todo.utils.validators.TaskValidator;
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

    public Boolean userHasAccess(User user, Task task){
        Boolean userIsUser = user.equals(task.getUser());
        Boolean admin = user.isAdmin();
        return userIsUser || admin;
    }

    public void checkAccess(DataStorage storage, User user, Task task){
        if(!userHasAccess(user, task)) storage.putException("access", new AccessDeniedException("Access denied for the user!"));
    }

    public Task getTask(DataStorage storage, Long id){
        // find the task
        var taskValidatorVar = (TaskValidator) taskValidator;
        DataStorage storageTask = taskValidatorVar.getTask(id);
        storage.fill(storageTask);
        Task task = null;
        if(!storageTask.hasExceptions()) task = (Task) storage.getData("task");
        return task;
    }

    public String getText(DataStorage storage, Map<String, String> form){ // it must be in common Service
        // validate a text
        DataStorage storageText = taskValidator.getString(form, "text");
        storage.fill(storageText);
        String text = null;
        if(!storageText.hasExceptions()) text = (String) storageText.getData("text");
        return text;
    }

    public Category getCategory(DataStorage storage, Map<String, String> form){ // it must be in CategoryService
        // validate a category
        var categoryValidatorVar = (CategoryValidator) categoryValidator;
        DataStorage storageCategory = categoryValidatorVar.getCategory(form);
        storage.fill(storageCategory);
        Category category = null;
        if(!storageCategory.hasExceptions()) category = (Category) storageCategory.getData("category");
        return category;
    }

    public Boolean getCompleted(DataStorage storage, Map<String, String> form){
        // validate completed field
        var taskValidatorVar = (TaskValidator) taskValidator;
        DataStorage storageCompleted = taskValidatorVar.getCompleted(form);
        storage.fill(storageCompleted);
        Boolean completed = (Boolean) storage.getData("completed");
        return completed;
    }

    public DataStorage create(User user, Map<String, String> form) throws RuntimeException{
        DataStorage storage = (DataStorage) context.getBean(DataStorage.class);

        // validate values
        String text = getText(storage, form);
        Category category = getCategory(storage, form);

        // check if text or category are not valid
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

    public DataStorage update(Long id, User user, Map<String, String> form){
        DataStorage storage = (DataStorage) context.getBean(DataStorage.class);

        // find the task to update
        Task task = getTask(storage, id);

        // check if task is not found
        if(storage.hasExceptions()) return storage;

        // check if user has access
        checkAccess(storage, user, task);
        if(storage.hasExceptions()) return storage;

        // validate values
        String text = getText(storage, form);
        Category category = getCategory(storage, form);
        Boolean completed = getCompleted(storage, form);

        // check if text, category or completed are not valid
        if(storage.hasExceptions()) return storage;
        
        // update the task
        // new task must be created -> it will be realized soon
        // user and createdDate are already set
        task.setText(text);
        task.setCompleted(completed);
        task.setModifiedDate(Instant.now());
        task.setCategory(category);
        // save the task
        taskRepository.save(task);
        return storage;
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
