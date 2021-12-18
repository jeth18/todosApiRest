package com.projet.todos.Repository;

import com.projet.todos.models.Response;
import com.projet.todos.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ITodoRespository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByIdUser(Long id);
}
