package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.services.CategoryService;
import com.todo.todo.utils.dto.DataStorage;

@Component
@Qualifier("CategoryValidator")
public class CategoryValidator extends Validator{

    @Autowired // should be lazy
    private CategoryService categoryService;

    @Autowired
    private ApplicationContext context;
    
    public DataStorage getCategory(Map<String, String> form){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        storage.fill(getLong(form, "category"));
        if(storage.hasExceptions()){
            storage.removeData("category");
            return storage;
        }
        Long id = (Long) storage.getData("category");
        storage.fill(getCategory(id));
        if(storage.hasExceptions()){
            storage.removeData("category");
            return storage;
        }
        return storage;
    }

    public DataStorage getCategory(Long id){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        try{
            Category category = categoryService.findById(id);
            storage.putData("category", category);
        } catch(EntityNotFoundByIdException categoryNotFound){
            storage.putException("category", categoryNotFound);
        }
        return storage;
    }

}
