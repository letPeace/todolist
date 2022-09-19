package com.todo.todo.models;

import java.time.Instant;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.todo.todo.models.singletons.UserSingleton;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Fill a title of a category")
    private String title;

    private Instant createdDate;
    
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Task> tasks;

    public Category() {
    }

    public Category(String title) {
        this(title, UserSingleton.get());
    }

    public Category(String title, User user){
        this(title, Instant.now(), Instant.now(), user);
    }

    public Category(String title, Instant createdDate, Instant modifiedDate, User user) {
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Category{id=%d, title='%s', createdDate='%s', modifiedDate='%s'}",
        id, title, createdDate, modifiedDate);
    }

}
