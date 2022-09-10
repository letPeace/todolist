package com.todo.todo.models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Fill a text for a task")
    private String text;

    private Boolean completed;

    private Instant createdDate;
    
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {
    }

    public Task(String text){
        this(text, new User("usernameNONE", "passwordNONE"));
    }

    public Task(String text, User user) {
        this.text = text;
        this.completed = Boolean.FALSE;
        this.createdDate = Instant.now();
        this.modifiedDate = Instant.now();
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, text='%s', completed='%s', createdDate='%s', modifiedDate='%s'}",
        id, text, completed, createdDate, modifiedDate);
    }

}
