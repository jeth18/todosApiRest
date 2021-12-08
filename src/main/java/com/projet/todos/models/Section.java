package com.projet.todos.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "El nombre es requerido")
    @Column(nullable = false)
    private String name;

    @Valid
    @NotNull(message = "Lista requerida")
    @OneToMany( targetEntity=Todo.class )
    private List<Todo> todoList;
}
