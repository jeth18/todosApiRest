package com.projet.todos.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Todo {
    @Id
    private Long id;
    private String title;
    private String description;
    private boolean active;
}
