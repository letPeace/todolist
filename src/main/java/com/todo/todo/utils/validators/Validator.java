package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.ObjectIsEmptyException;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.Values;

@Component
public class Validator {
    
    protected Boolean exists(Map<String, String> form, String key){
        return form.containsKey(key);
    }
    
    public DataStorage getValue(Map<String, String> form, String key){
        DataStorage storage = BeanFactory.dataStorage();
        if(!exists(form, key)) storage.putException(key, new NullPointerException(Values.KEY_NOT_EXISTS+key));
        else storage.putData(key, form.get(key));
        return storage;
    }

    public DataStorage getString(String fieldName, String string){
        DataStorage storage = BeanFactory.dataStorage();
        if(string.isBlank()){
            storage.putException(fieldName, new ObjectIsEmptyException(Values.FIELD_EMPTY+fieldName)); // @NotBlank from User/Task/Category should be used here
        }
        else storage.putData(fieldName, string.trim());
        return storage;
    }

    public DataStorage getString(Map<String, String> form, String key){
        DataStorage storage = BeanFactory.dataStorage();
        // validate if the value exists
        storage.fill(getValue(form, key));
        // return if cannot get value
        if(storage.hasException(key)) return storage;
        // validate if the string is not empty
        storage.fill(getString(key, (String) storage.getData(key))); // perhaps [ storage = getString(...); ] is better
        // remove 'key' from data if its exception appears
        if(storage.hasException(key)) storage.removeData(key);
        return storage;
    }

    public DataStorage getLong(Map<String, String> form, String key){
        DataStorage storage = BeanFactory.dataStorage();
        // validate if the value exists
        storage.fill(getValue(form, key));
        // return if cannot get value
        if(storage.hasExceptions()) return storage;
        // validate if the string can be parsed to long
        storage.fill(getLong(key, (String) storage.getData(key)));
        return storage;
    }

    public DataStorage getLong(String fieldName, String idString){
        DataStorage storage = BeanFactory.dataStorage();
        Long number = 0L;
        try{
            number = Long.parseLong(idString);
        } catch(NumberFormatException parseLongException){
            storage.putException(fieldName, parseLongException);
        } catch(NullPointerException stringIsNull){
            storage.putException(fieldName, stringIsNull);
        }
        if(!storage.hasExceptions()){
            storage.putData(fieldName, number);
        }
        return storage;
    }

}
