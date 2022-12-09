package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.services.CategoryService;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.Values;

@Component
@Qualifier("CategoryValidator")
public class CategoryValidator extends Validator{

    @Lazy
    @Autowired
    private CategoryService categoryService;
    
    public DataStorage getCategory(Map<String, String> form){
        DataStorage storage = BeanFactory.dataStorage();
        storage.fill(getLong(form, Values.CATEGORY));
        if(storage.hasExceptions()){
            storage.removeData(Values.CATEGORY);
            return storage;
        }
        Long id = (Long) storage.getData(Values.CATEGORY);
        storage.fill(getCategory(id));
        if(storage.hasExceptions()){
            storage.removeData(Values.CATEGORY);
            return storage;
        }
        return storage;
    }

    public DataStorage getCategory(Long id){
        DataStorage storage = BeanFactory.dataStorage();
        try{
            Category category = categoryService.findById(id);
            storage.putData(Values.CATEGORY, category);
        } catch(EntityNotFoundByIdException categoryNotFound){
            storage.putException(Values.CATEGORY, categoryNotFound);
        }
        return storage;
    }

}
