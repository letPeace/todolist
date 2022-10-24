package com.todo.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.todo.todo.utils.dto.Data;
import com.todo.todo.utils.dto.DataStorage;

@Configuration
public class DataStorageConfig {

    @Bean
    @Scope(value = "prototype")
    public DataStorage dataStorage(@Autowired Data<String, Object> data, @Autowired Data<String, Exception> exceptions){
        return new DataStorage(data, exceptions);
    }
    
}
