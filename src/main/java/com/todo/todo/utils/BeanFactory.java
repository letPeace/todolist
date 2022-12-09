package com.todo.todo.utils;

import com.todo.todo.utils.enums.Values;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.todo.todo.utils.dto.DataStorage;

public abstract class BeanFactory {

    @Autowired
    private static ApplicationContext context;
    
    public static DataStorage dataStorage(){
        return (DataStorage) context.getBean(Values.DATA_STORAGE);
    }

}
