package com.projet.todos.Repository;

import com.projet.todos.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITodoRespository extends JpaRepository<Todo, Long> {
    
}
