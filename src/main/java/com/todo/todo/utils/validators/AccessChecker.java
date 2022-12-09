package com.todo.todo.utils.validators;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.todo.todo.models.HavingUser;
import com.todo.todo.models.User;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.Values;

@Component
public class AccessChecker<T extends HavingUser> {
    
    // does user has access to T
    public Boolean has(User user, T t){
        Boolean userIsUser = user.equals(t.getUser());
        Boolean admin = user.isAdmin();
        return userIsUser || admin;
    }

    // fill storage with info about user has access to T or not
    public void check(DataStorage storage, User user, T t){
        if(!has(user, t)) storage.putException(Values.ACCESS, new AccessDeniedException(Values.ACCESS_DENIED));
    }

    public DataStorage check(User user, T t){
        DataStorage storage = BeanFactory.dataStorage();
        check(storage, user, t);
        return storage;
    }

}
