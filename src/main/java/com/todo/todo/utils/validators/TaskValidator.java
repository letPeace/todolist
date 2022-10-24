package com.todo.todo.utils.validators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("TaskValidator")
public class TaskValidator extends Validator{
    
}
