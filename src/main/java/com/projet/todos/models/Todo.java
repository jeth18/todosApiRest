package com.projet.todos.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "El titulo es requerido")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "La descripción es requerida")
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean active;

    @NotNull
    private Long idUser;

    protected Todo() { }
}
