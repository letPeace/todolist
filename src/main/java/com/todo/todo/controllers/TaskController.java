package com.todo.todo.controllers;

import java.util.List;
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
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.services.CategoryService;
import com.todo.todo.services.TaskService;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.CategoryValues;
import com.todo.todo.utils.enums.TaskValues;
import com.todo.todo.utils.enums.Values;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CategoryService categoryService;

    /*
     * MAPPING METHODS
     */

    @GetMapping("/create")
    public ModelAndView getCreateTaskPage(ModelAndView model, @AuthenticationPrincipal User user){
        List<Category> categories = categoryService.findAllByUser(user);
        if(categories.isEmpty()){
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            model.addObject(Values.CATEGORY_ERROR, CategoryValues.NO_CATEGORIES_EXIST);
            return model;
        }
        model.setViewName(TaskValues.CREATE_PAGE);
        model.addObject(Values.TEXT, Values.TEXT_DEFAULT);
        model.addObject(Values.CATEGORIES, categories);
        model.addObject(Values.CATEGORY_SELECTED, categories.get(0));
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    ModelAndView model){
        DataStorage storage = taskService.create(user, form);
        if(!storage.hasExceptions()){
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        // storage has exceptions
        model.setViewName(TaskValues.CREATE_PAGE);

        fillModelByText(storage, model);

        // set categories field
        List<Category> categories = categoryService.findAllByUser(user); // should check if user does not have any category
        model.addObject(Values.CATEGORIES, categories);

        fillModelByCategory(storage, model, categories.get(0));
        return model;
    }
    
    @GetMapping("/update/{id}")
    public ModelAndView getUpdateTaskPage(@AuthenticationPrincipal User user, 
                                            @PathVariable(value="id") Long id, // should check if id is long ?
                                            ModelAndView model){
        // check if task exists and user has access to it
        DataStorage storage = taskService.get(user, id);
        if(storage.hasExceptions()){
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        model.setViewName(TaskValues.UPDATE_PAGE);
        model.addObject(Values.ID, id);
        Task task = (Task) storage.getData(Values.TASK);
        model.addObject(Values.TEXT, task.getText());
        model.addObject(Values.COMPLETED, task.getCompleted());
        model.addObject(Values.CATEGORIES, categoryService.findAllByUser(user)); // it might be safe due to that task exist if and only if at least one category exists
        model.addObject(Values.CATEGORY_SELECTED, task.getCategory());
        return model;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    @PathVariable(value="id") Long id, // should check if id is long ?
                                    ModelAndView model){
        DataStorage storage = taskService.update(user, id, form);
        if(!storage.hasExceptions() 
            || storage.hasException(Values.ACCESS)
            || storage.hasException(Values.TASK)){
            // storage does not have exceptions, has access exception or task is not found
            model.setViewName(Values.REDIRECT_HOME_PAGE);
            return model;
        }
        // storage has exceptions
        model.setViewName(TaskValues.UPDATE_PAGE); 
        
        // set id
        model.addObject(Values.ID, id);

        fillModelByText(storage, model);

        // set categories field
        List<Category> categories = categoryService.findAllByUser(user);
        model.addObject(Values.CATEGORIES, categories);

        fillModelByCategory(storage, model, ((Task) storage.getData(Values.TASK)).getCategory());

        fillModelByCompleted(storage, model);
        return model;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteTask(@AuthenticationPrincipal User user, 
                                    @PathVariable(value="id") Long id, // should check if id is long ?
                                    ModelAndView model){
        model.setViewName(Values.REDIRECT_HOME_PAGE);
        DataStorage storage = taskService.delete(user, id);
        if(storage.hasExceptions()){
            // set some error to model
        }
        return model;
    }

    /*
     * METHODS FILLING MODEL
     */

    private void fillModelByText(DataStorage storage, ModelAndView model){
        // set text field
        if(storage.hasData(Values.TEXT)){
            model.addObject(Values.TEXT, storage.getData(Values.TEXT));
        } else{
            model.addObject(Values.TEXT_ERROR, storage.getException(Values.STRING));
            model.addObject(Values.TEXT, Values.TEXT_DEFAULT);
        }
    }

    private void fillModelByCategory(DataStorage storage, ModelAndView model, Category category){
        // set categorySelected field
        if(storage.hasData(Values.CATEGORY)){
            model.addObject(Values.CATEGORY_SELECTED, storage.getData(Values.CATEGORY));
        } else{
            model.addObject(Values.CATEGORY_ERROR, storage.getException(Values.CATEGORY));
            model.addObject(Values.CATEGORY_SELECTED, category);
        }
    }

    private void fillModelByCompleted(DataStorage storage, ModelAndView model){
        if(storage.hasData(Values.COMPLETED)){
            model.addObject(Values.COMPLETED, storage.getData(Values.COMPLETED));
        }
    }

}
