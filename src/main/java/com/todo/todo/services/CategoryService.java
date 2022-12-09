package com.todo.todo.services;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.todo.todo.exceptions.EntityNotFoundByIdException;
import com.todo.todo.models.Category;
import com.todo.todo.models.User;
import com.todo.todo.repositories.CategoryRepository;
import com.todo.todo.utils.BeanFactory;
import com.todo.todo.utils.dto.DataStorage;
import com.todo.todo.utils.enums.CategoryValues;
import com.todo.todo.utils.enums.Values;
import com.todo.todo.utils.validators.AccessChecker;
import com.todo.todo.utils.validators.CategoryValidator;
import com.todo.todo.utils.validators.Validator;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    @Lazy
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    private TaskService taskService;

    @Autowired
    @Qualifier("CategoryValidator")
    private Validator categoryValidator;

    @Autowired
    private AccessChecker<Category> accessChecker;

    /*
     * METHODS CALLING REPOSITORY
     */

    public void save(Category category){
        categoryRepository.save(category);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public List<Category> findAllByUser(User user){
        return categoryRepository.findAllByUser(user.getId());
    }

    public Category findById(Long id) throws RuntimeException{
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(CategoryValues.NOT_FOUND + id));
    }

    /*
     * METHODS CALLED BY CONTROLLER
     */

    public DataStorage get(User user, Long id){
        DataStorage storage = BeanFactory.dataStorage();

        Category category = getCategory(storage, id);

        // check if category is not found
        if(storage.hasExceptions()) return storage;

        // check if user has access
        accessChecker.check(storage, user, category);

        return storage;
    }
    
    public DataStorage create(User user, Map<String, String> form) throws RuntimeException{
        DataStorage storage = BeanFactory.dataStorage();

        // validate values
        String title = getTitle(storage, form);

        // check if title is not valid
        if(storage.hasExceptions()) return storage;

        // create a new category
        Category category = new Category(); // DI must be here
        category.setTitle(title);
        category.setCreatedDate(Instant.now());
        category.setModifiedDate(Instant.now());
        category.setUser(user);
        // save the category
        categoryRepository.save(category);
        return storage;
    }

    public DataStorage update(User user, Long id, Map<String, String> form){
        DataStorage storage = BeanFactory.dataStorage();

        // find the category to update
        Category category = getCategory(storage, id);

        // check if category is not found
        if(storage.hasExceptions()) return storage;

        // check if user has access
        accessChecker.check(storage, user, category);
        if(storage.hasExceptions()) return storage;

        // validate values
        String title = getTitle(storage, form);

        // check if title is not valid
        if(storage.hasExceptions()) return storage;
        
        // update the category
        // new category must be created -> it will be realized soon
        // user and createdDate are already set
        category.setTitle(title);
        category.setModifiedDate(Instant.now());
        // save the category
        categoryRepository.save(category);
        return storage;
    }

    public DataStorage delete(User user, Long id){
        // find category by id, then check access
        DataStorage storage = get(user, id);
        
        // check if task is not found or user does not have access to it
        if(storage.hasExceptions()) return storage;

        storage.fill(delete((Category) storage.getData(Values.CATEGORY)));

        return storage;
    }

    protected DataStorage delete(Category category){ // category must be valid
        DataStorage storage = BeanFactory.dataStorage();

        User user = category.getUser();
        user.getCategories().remove(category);
        userService.save(user);

        taskService.deleteAll(category.getTasks());

        categoryRepository.delete(category);
        return storage;
    }

    protected DataStorage deleteAll(Set<Category> categories){ // categories must be valid
        DataStorage storage = BeanFactory.dataStorage();

        Set<Category> categoriesCopy = new HashSet<>(categories);

        for(Category category : categoriesCopy){
            storage.fill(delete(category));
            if(storage.hasExceptions()) return storage;
        }

        return storage;
    }

    /*
     * METHODS CALLING VALIDATOR
     */

    public Category getCategory(DataStorage storage, Long id){
        // validate the category
        var categoryValidatorVar = (CategoryValidator) categoryValidator;
        DataStorage storageCategory = categoryValidatorVar.getCategory(id);
        storage.fill(storageCategory);
        Category category = null;
        if(!storageCategory.hasExceptions()) category = (Category) storage.getData(Values.CATEGORY);
        return category;
    }

    public String getTitle(DataStorage storage, Map<String, String> form){
        // validate a title
        DataStorage storageTitle = categoryValidator.getString(form, Values.TITLE);
        storage.fill(storageTitle);
        String title = null;
        if(!storageTitle.hasExceptions()) title = (String) storageTitle.getData(Values.TITLE);
        return title;
    }

}
