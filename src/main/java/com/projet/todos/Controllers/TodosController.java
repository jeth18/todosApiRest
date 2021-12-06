package com.projet.todos.Controllers;

import com.projet.todos.Repository.ITodoRespository;
import com.projet.todos.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TodosController {
    @Autowired
    private ITodoRespository repo;

    @RequestMapping(value = "/todos")
    public ResponseEntity<Object> findAll(){
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
}
