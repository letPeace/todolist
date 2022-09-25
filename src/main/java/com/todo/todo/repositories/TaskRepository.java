package com.todo.todo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.todo.todo.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{
    
    @Query("FROM Task t WHERE t.user.id = :userId")
    List<Task> findAllByUser(@Param("userId") Long userId);

}
