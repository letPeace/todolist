package com.todo.todo.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.todo.todo.models.Category;
import com.todo.todo.models.Task;
import com.todo.todo.models.User;
import com.todo.todo.repositories.CategoryRepository;
import com.todo.todo.repositories.TaskRepository;
import com.todo.todo.repositories.UserRepository;

@Component
public class TaskDataLoader implements CommandLineRunner{

    private final Logger logger = LoggerFactory.getLogger(TaskDataLoader.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData(){
        if(userRepository.count() == 0 || categoryRepository.count() == 0 || taskRepository.count() == 0){
            User user1 = new User("name1", "password1");
            User user2 = new User("name2", "password2");
            userRepository.saveAll(Arrays.asList(user1, user2));
            //
            Category category1 = new Category("Category1 -> user1", user1);
            Category category2 = new Category("Category2 -> user1", user1);
            Category category3 = new Category("Category3 -> user2", user2);
            categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
            //
            Task task1 = new Task("Task1 -> user1, category1", user1, category1);
            Task task2 = new Task("Task2 -> user1, category1", user1, category1);
            Task task3 = new Task("Task3 -> user1, category2", user1, category2);
            task3.setCompleted(Boolean.TRUE);
            Task task4 = new Task("Task4 -> user2, category3", user2, category3);
            Task task5 = new Task("Task5 -> user2, category3", user2, category3);
            task5.setCompleted(Boolean.TRUE);
            taskRepository.saveAll(Arrays.asList(task1, task2, task3, task4, task5));
        }
        logger.info("TASKS number -> {}", taskRepository.count());
        logger.info("USERS number -> {}", userRepository.count());
        logger.info("CATEGORIES number -> {}", categoryRepository.count());
    }
    
}
