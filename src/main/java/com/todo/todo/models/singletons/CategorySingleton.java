package com.todo.todo.models.singletons;

import com.todo.todo.models.Category;

public abstract class CategorySingleton {
    
    private static Category category;
    
    public static Category get(){
        return category == null ? new Category("categoryNONE") : category;
    } 

}
