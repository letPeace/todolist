package com.todo.todo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.services.CategoryService;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.CategoryValues;
import com.todo.todo.utils.enums.Values;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
     * MAPPING METHODS
     */

    @GetMapping("/create")
    public ModelAndView getCreateCategoryPage(ModelAndView model){
        model.setViewName(CategoryValues.CREATE_PAGE);
        model.addObject(Values.TITLE, Values.TITLE_DEFAULT);
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    ModelAndView model){
        DataStorage storage = categoryService.create(user, form);
        if(!storage.hasExceptions()){
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        // storage has exceptions
        model.setViewName(CategoryValues.CREATE_PAGE);

        fillModelByTitle(storage, model);

        return model;
    }

    @GetMapping("/update/{id}")
    public ModelAndView getUpdateCategoryPage(@AuthenticationPrincipal User user, 
                                                @PathVariable(value="id") Long id, // should check if id is long ?
                                                ModelAndView model){
        // check if category exists and user has access to it
        DataStorage storage = categoryService.get(user, id);
        if(storage.hasExceptions()){
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        model.setViewName(CategoryValues.UPDATE_PAGE);
        model.addObject(Values.ID, id);
        model.addObject(Values.TITLE, ((Category) storage.getData(Values.CATEGORY)).getTitle());
        return model;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    @PathVariable(value="id") Long id, // should check if id is long ?
                                    ModelAndView model){
        DataStorage storage = categoryService.update(user, id, form);
        if(!storage.hasExceptions() 
            || storage.hasException(Values.ACCESS)
            || storage.hasException(Values.CATEGORY)){
            // storage does not have exceptions, has access exception or category is not found
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        // storage has exceptions
        model.setViewName(CategoryValues.UPDATE_PAGE); 
        
        // set id
        model.addObject(Values.ID, id);

        fillModelByTitle(storage, model);

        return model;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteCategory(@AuthenticationPrincipal User user, 
                                        @PathVariable(value="id") Long id, // should check if id is long ?
                                        ModelAndView model){
        model.setViewName(Values.REDIRECT_HOME_PAGE);
        DataStorage storage = categoryService.delete(user, id);
        if(storage.hasExceptions()){
            // set some error to model
        }
        return model;
    }

    /*
     * METHODS FILLING MODEL
     */

    private void fillModelByTitle(DataStorage storage, ModelAndView model){
        // set title field
        if(storage.hasData(Values.TITLE)){
            model.addObject(Values.TITLE, storage.getData(Values.TITLE));
        } else{
            model.addObject(Values.TITLE_ERROR, storage.getException(Values.STRING));
            model.addObject(Values.TITLE, Values.TITLE_DEFAULT);
        }
    }

}
