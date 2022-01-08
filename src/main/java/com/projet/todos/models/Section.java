package com.projet.todos.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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
    @OneToMany( targetEntity=Todo.class, cascade = CascadeType.ALL)
    private Set<Todo> todoList = new HashSet<>();

    @NotNull
    private Long idUser;

}
