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
public class Task implements HavingUser {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    public Task() {
    }

    /*public Task(String text){
        this(text, UserSingleton.get(), CategorySingleton.get());
    }*/

    public Task(String text, User user, Category category) {
        this(text, Boolean.FALSE, Instant.now(), Instant.now(), user, category);
    }

    public Task(String text, Boolean completed, Instant createdDate, Instant modifiedDate, User user, Category category) {
        this.text = text;
        this.completed = completed;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = user;
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, text='%s', completed='%s', createdDate='%s', modifiedDate='%s'}",
        id, text, completed, createdDate, modifiedDate);
    }

}
