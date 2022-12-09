package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Task;
import com.todo.todo.services.TaskService;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.Values;

@Component
@Qualifier("TaskValidator")
public class TaskValidator extends Validator{

    @Lazy
    @Autowired
    private TaskService taskService;

    public DataStorage getTask(Long id){
        DataStorage storage = BeanFactory.dataStorage();
        try{
            Task task = taskService.findById(id);
            storage.putData(Values.TASK, task);
        } catch(EntityNotFoundByIdException exception){
            storage.putException(Values.TASK, exception);
        }
        return storage;
    }

    public DataStorage getCompleted(Map<String, String> form){
        DataStorage storage = BeanFactory.dataStorage();
        if(exists(form, Values.COMPLETED)) storage.putData(Values.COMPLETED, Boolean.TRUE);
        else storage.putData(Values.COMPLETED, Boolean.FALSE);
        // maybe it is logically incorrect and this field must exist always, but have two possible values: true or false (in HTML)
        return storage;
    }
    
}
