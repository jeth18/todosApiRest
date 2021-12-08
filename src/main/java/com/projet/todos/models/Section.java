package com.projet.todos.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany( targetEntity=Todo.class )
    private List<Todo> todoList;
}
