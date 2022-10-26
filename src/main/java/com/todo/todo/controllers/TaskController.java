package com.todo.todo.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final String updatePage = "update_task";
    private final String createPage = "create_task";
    private final String redirectHomePage = "redirect:/users/home";

    @Autowired
    private TaskService taskService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    public ModelAndView getCreateTaskPage(ModelAndView model, @AuthenticationPrincipal User user){
        List<Category> categories = categoryService.findAllByUser(user);
        if(categories.isEmpty()){
            model.setViewName(redirectHomePage);
            model.addObject("error", "At least one category must exist to create a task!");
            return model;
        }
        model.setViewName(createPage);
        model.addObject("text", "To do text");
        model.addObject("categories", categories);
        model.addObject("categorySelected", categories.get(0));
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    ModelAndView model){
        DataStorage storage = taskService.create(user, form);
        if(!storage.hasExceptions()){
            model.setViewName(redirectHomePage);
            return model;
        }
        // storage has exceptions
        model.setViewName(createPage);

        setText(storage, model);

        // set categories field
        List<Category> categories = categoryService.findAllByUser(user); // should check if user does not have any category
        model.addObject("categories", categories);

        setCategory(storage, model, categories.get(0));
        return model;
    }
    
    @GetMapping("/update/{task}")
    public ModelAndView getUpdateTaskPage(@Valid Task task, 
                                            BindingResult result, 
                                            @AuthenticationPrincipal User user, 
                                            ModelAndView model){
        if(!(user.equals(task.getUser()) || user.isAdmin())){
            model.setViewName(redirectHomePage);
            model.addObject("error", "Access denied!");
            return model;
        }
        model.setViewName(updatePage);
        model.addObject("id", task.getId());
        model.addObject("text", task.getText());
        model.addObject("completed", task.getCompleted());
        model.addObject("task", task);
        model.addObject("categories", categoryService.findAllByUser(user));
        model.addObject("categorySelected", task.getCategory());
        return model;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateTask(@AuthenticationPrincipal User user, 
                                    @RequestParam Map<String, String> form, 
                                    @PathVariable(value="id") Long id, // should check if id is long ?
                                    ModelAndView model){
        DataStorage storage = taskService.update(id, user, form);
        if(!storage.hasExceptions() 
            || storage.hasException("access")
            || storage.hasException("task")){
            // storage does not have exceptions, has access exception or task is not found
            model.setViewName(redirectHomePage);
            return model;
        }
        // storage has exceptions
        model.setViewName(updatePage);
        
        // set id
        model.addObject("id", id);

        setText(storage, model);

        // set categories field
        List<Category> categories = categoryService.findAllByUser(user);
        model.addObject("categories", categories);

        setCategory(storage, model, ((Task) storage.getData("task")).getCategory());

        setCompleted(storage, model);
        return model;
    }

    @PostMapping("/delete/{task}")
    public ModelAndView deleteTask(@Valid Task task, 
                                    BindingResult result, 
                                    @AuthenticationPrincipal User user, 
                                    ModelAndView model){
        model.setViewName(redirectHomePage);
        if(!(user.equals(task.getUser()) || user.isAdmin())) return model;
        taskService.delete(task, result);
        return model;
    }

    private void setText(DataStorage storage, ModelAndView model){
        // set text field
        if(storage.hasData("text")){
            model.addObject("text", storage.getData("text"));
        } else{
            model.addObject("textError", storage.getException("string"));
            model.addObject("text", "Task text (autogenerated)");
        }
    }

    private void setCategory(DataStorage storage, ModelAndView model, Category category){
        // set categorySelected field
        if(storage.hasData("category")){
            model.addObject("categorySelected", storage.getData("category"));
        } else{
            model.addObject("categoryError", storage.getException("category"));
            model.addObject("categorySelected", category);
        }
    }

    private void setCompleted(DataStorage storage, ModelAndView model){
        if(storage.hasData("completed")){
            model.addObject("completed", storage.getData("completed"));
        }
    }

}
