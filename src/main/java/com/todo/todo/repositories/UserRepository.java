package com.todo.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todo.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
