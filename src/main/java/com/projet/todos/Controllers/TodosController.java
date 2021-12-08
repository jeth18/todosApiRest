package com.projet.todos.Controllers;

import com.projet.todos.Repository.ITodoRespository;
import com.projet.todos.models.Response;
import com.projet.todos.models.Todo;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class TodosController {
    @Autowired
    private ITodoRespository repo;

    Logger log = Logger.getLogger(TodosController.class.getName());
    Response response;
    private HttpStatus status = null;


    @RequestMapping(value = "/todo")
    ResponseEntity<Response> findAll(){
        response = new Response(repo.findAll(), 200, "Success");
        log.info("Get todos: "+ response.getData());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/todo/{id}")
    ResponseEntity<Response> findById(@PathVariable(required = true) Long id) {
        response = new Response(repo.findById(id), 200, "Success");
        log.info("Get todo: " + response.getData());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/todo")
    ResponseEntity<Response> newTodo(@RequestBody Todo todo) {
        response = new Response(repo.save(todo), 200, "Success");
        log.info("Todo agregada: "+ response.getData());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/todo/{id}")
    ResponseEntity<Response> updateTodo(@RequestBody(required = true) Todo todo, @PathVariable(required = true) Long id) throws Exception {
        if (todo.getId() !=  id || id < 0) {
            status = HttpStatus.BAD_REQUEST;
            response = new Response("Could not update the todo", 400, "Error");
        } else {
            if(!repo.existsById(id)) {
                status = HttpStatus.BAD_REQUEST;
                response = new Response("Not found todo", 400, "Error");
            } else {
                status = HttpStatus.OK;
                log.info("Todo update: "+ response.getData());
                response = new Response(repo.save(todo), 200, "Success");
            }
        }
        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/todo/{id}")
    ResponseEntity<Response> deleteTodo(@PathVariable(required = true) Long id) {
        if( id <= 0 ) {
            status = HttpStatus.BAD_REQUEST;
            response = new Response("Could not delete the todo", 400, "Error");
        } else {
            status = HttpStatus.OK;
            repo.deleteById(id);
            log.info("Todo delete: " + id);
            response = new Response(200, "Success");
        }

        return new ResponseEntity<>(response, status);
    }
}
