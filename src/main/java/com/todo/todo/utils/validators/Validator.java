package com.todo.todo.utils.validators;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.todo.todo.exceptions.ObjectIsEmptyException;
import com.todo.todo.utils.dto.DataStorage;

@Component
public class Validator {

    @Autowired
    private ApplicationContext context;
    
    public DataStorage getValue(Map<String, String> form, String key){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        if(!form.containsKey(key)) storage.putException(key, new NullPointerException("No '"+key+"' key existing!"));
        else storage.putData(key, form.get(key));
        return storage;
    }

    public DataStorage getString(String fieldName, String string){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        if(string.isBlank()){
            storage.putException(fieldName, new ObjectIsEmptyException("Field '"+fieldName+"' is empty!")); // @NotBlank from Task.java should be used here
        }
        else storage.putData(fieldName, string.trim());
        return storage;
    }

    public DataStorage getString(Map<String, String> form, String key){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
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
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
        // validate if the value exists
        storage.fill(getValue(form, key));
        // return if cannot get value
        if(storage.hasExceptions()) return storage;
        // validate if the string can be parsed to long
        storage.fill(getLong(key, (String) storage.getData(key)));
        return storage;
    }

    public DataStorage getLong(String fieldName, String idString){
        DataStorage storage = (DataStorage) context.getBean("dataStorage");
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
