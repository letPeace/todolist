package com.todo.todo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.todo.todo.models.Task;
import com.todo.todo.repositories.TaskRepository;

@Component
public class TaskDataLoader implements CommandLineRunner{

    private final Logger logger = LoggerFactory.getLogger(TaskDataLoader.class);

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSeedData();
    }

    private void loadSeedData(){
        if (taskRepository.count() == 0) {
            Task task1 = new Task("Start using this application");
            Task task2 = new Task("Like it and be happy");
            Task task3 = new Task("Start completing all tasks");
            task3.setCompleted(Boolean.TRUE);
            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
        }
        logger.info("NOTE: {} tasks exists!", taskRepository.count());
    }
    
}
