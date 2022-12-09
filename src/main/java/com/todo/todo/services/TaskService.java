package com.todo.todo.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.TaskRepository;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.TaskValues;
import com.todo.todo.utils.enums.Values;
import com.todo.todo.utils.validators.AccessChecker;
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
    private AccessChecker<Task> accessChecker;

    /*
     * METHODS CALLING REPOSITORY
     */

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
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(TaskValues.NOT_FOUND + id));
    }

    /*
     * METHODS CALLED BY CONTROLLER
     */

    public DataStorage get(User user, Long id){
        DataStorage storage = BeanFactory.dataStorage();

        Task task = getTask(storage, id);

        // check if task is not found
        if(storage.hasExceptions()) return storage;

        // check if user has access
        accessChecker.check(storage, user, task);

        return storage;
    }

    public DataStorage create(User user, Map<String, String> form) throws RuntimeException{
        DataStorage storage = BeanFactory.dataStorage();

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

    public DataStorage update(User user, Long id, Map<String, String> form){
        DataStorage storage = BeanFactory.dataStorage();

        // find the task to update
        Task task = getTask(storage, id);

        // check if task is not found
        if(storage.hasExceptions()) return storage;

        // check if user has access
        accessChecker.check(storage, user, task);
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

    public DataStorage delete(User user, Long id){
        // find task by id, then check access
        DataStorage storage = get(user, id);

        // check if task is not found or user does not have access to it
        if(storage.hasExceptions()) return storage;

        storage.fill(delete((Task) storage.getData(Values.TASK)));

        return storage;
    }

    protected DataStorage delete(Task task){ // task must be valid
        DataStorage storage = BeanFactory.dataStorage();

        Category category = task.getCategory();
        if(category != null){ // is it possible ?
            category.getTasks().remove(task);
            categoryService.save(category);
        }
        
        User user = task.getUser();
        user.getTasks().remove(task);
        userService.save(user);

        taskRepository.delete(task);
        return storage;
    }

    protected DataStorage deleteAll(Set<Task> tasks){ // tasks must be valid
        DataStorage storage = BeanFactory.dataStorage();

        Set<Task> tasksCopy = new HashSet<>(tasks);

        for(Task task : tasksCopy){
            storage.fill(delete(task));
            if(storage.hasExceptions()) return storage;
        }

        return storage;
    }
    
    /*
     * METHODS CALLING VALIDATOR
     */

    public Task getTask(DataStorage storage, Long id){
        // validate the task
        var taskValidatorVar = (TaskValidator) taskValidator;
        DataStorage storageTask = taskValidatorVar.getTask(id);
        storage.fill(storageTask);
        Task task = null;
        if(!storageTask.hasExceptions()) task = (Task) storage.getData(Values.TASK);
        return task;
    }

    public String getText(DataStorage storage, Map<String, String> form){ // it must be in common Service ?
        // validate a text
        DataStorage storageText = taskValidator.getString(form, Values.TEXT);
        storage.fill(storageText);
        String text = null;
        if(!storageText.hasExceptions()) text = (String) storageText.getData(Values.TEXT);
        return text;
    }

    public Category getCategory(DataStorage storage, Map<String, String> form){ // it must be in CategoryService ?
        // validate a category
        var categoryValidatorVar = (CategoryValidator) categoryValidator;
        DataStorage storageCategory = categoryValidatorVar.getCategory(form);
        storage.fill(storageCategory);
        Category category = null;
        if(!storageCategory.hasExceptions()) category = (Category) storageCategory.getData(Values.CATEGORY);
        return category;
    }

    public Boolean getCompleted(DataStorage storage, Map<String, String> form){
        // validate completed field
        var taskValidatorVar = (TaskValidator) taskValidator;
        DataStorage storageCompleted = taskValidatorVar.getCompleted(form);
        storage.fill(storageCompleted);
        Boolean completed = (Boolean) storage.getData(Values.COMPLETED);
        return completed;
    }

}
