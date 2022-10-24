package com.todo.todo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.todo.todo.utils.validators.CategoryValidator;
import com.todo.todo.utils.validators.TaskValidator;
import com.todo.todo.utils.validators.Validator;

@Configuration
public class ValidatorConfig {

    @Bean
    @Qualifier("CategoryValidator")
    public Validator createCategoryValidator(){
        return new CategoryValidator();
    }

    @Bean
    @Qualifier("TaskValidator")
    public Validator createTaskValidator(){
        return new TaskValidator();
    }
    
}
