package com.todo.todo.models.singletons;

import com.todo.todo.models.User;

public abstract class UserSingleton {

    private static User user;
    
    public static User get(){
        return user == null ? new User("usernameNONE", "passwordNONE") : user;
    } 

}
