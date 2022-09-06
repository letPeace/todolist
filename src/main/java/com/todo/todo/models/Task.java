package com.todo.todo.models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "task_name")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Fill a text for a task")
    private String text;

    @Getter
    @Setter
    private Boolean completed;

    @Getter
    @Setter
    private Instant createdDate;
    
    @Getter
    @Setter
    private Instant modifiedDate;

    public Task() {
    }

    public Task(String text) {
        this.text = text;
        this.completed = Boolean.FALSE;
        this.createdDate = Instant.now();
        this.modifiedDate = Instant.now();
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, text='%s', completed='%s', createdDate='%s', modifiedDate='%s'}",
        id, text, completed, createdDate, modifiedDate);
    }

}
