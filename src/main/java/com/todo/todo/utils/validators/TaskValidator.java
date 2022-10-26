package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Task;
import com.todo.todo.services.TaskService;
import com.todo.todo.utils.dto.DataStorage;

@Component
@Qualifier("TaskValidator")
public class TaskValidator extends Validator{

    @Lazy
    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationContext context;

    public DataStorage getTask(Long id){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        try{
            Task task = taskService.findById(id);
            storage.putData("task", task);
        } catch(EntityNotFoundByIdException exception){
            storage.putException("task", exception);
        }
        return storage;
    }

    public DataStorage getCompleted(Map<String, String> form){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        if(exists(form, "completed")) storage.putData("completed", Boolean.TRUE);
        else storage.putData("completed", Boolean.FALSE);
        // maybe it is logically incorrect and this field must exist always, but have two possible values: true or false (in HTML)
        return storage;
    }
    
}
