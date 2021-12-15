package com.projet.todos.Controllers;
import javax.validation.Valid;
import com.projet.todos.Repository.ISectionRepository;
import com.projet.todos.models.Response;
import com.projet.todos.models.Section;
import com.projet.todos.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Logger;

@RequestMapping("/api")
@RestController
@Validated
public class SectionController {

    @Autowired
    ISectionRepository repo;

    Logger log = Logger.getLogger(TodosController.class.getName());
    Response response;
    private HttpStatus status = null;

    @GetMapping("/section")
    ResponseEntity<Response> findAll() {
        response = new Response(repo.findAll(),200, "Success");
        log.info("Get All Section: " + response.getData());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/section")
    ResponseEntity<Response> save(@Valid @RequestBody Section section) {
        response = new Response(repo.save(section), 201, "Success");
        log.info("Section save: "+ response.getData());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/section/{id}/addTodo", method = RequestMethod.PATCH)
    ResponseEntity<Response> addTodoSection(@Valid @RequestBody Todo todo, @PathVariable(required = true) Long id) {
        if(id <= 0 || id == null ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Section section = repo.findById(id).get();
        Set<Todo> listTodo = section.getTodoList();
        listTodo.add(todo);

        response = new Response(repo.save(section), 200, "Success");
        log.info("Add todo: "+ response.getData());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/section/{id}")
    ResponseEntity<Response> updateSection(
            @Valid @RequestBody Section section,
            @PathVariable(required = true) Long id) {

        if(section.getId() != id || id <= 0) {
            status = HttpStatus.BAD_REQUEST;
            response = new Response("Could not update the todo", 400, "Error");
        } else {
            if(!repo.existsById(id)) {
                status = HttpStatus.BAD_REQUEST;
                response = new Response("Not found todo", 400, "Error");
            } else {
                status = HttpStatus.OK;
                Section sectionCopy = repo.findById(id).get();
                sectionCopy.setName(section.getName());
                response = new Response(repo.save(sectionCopy), 200, "Success");
                log.info("Section update: "+ response.getData());
            }
        }

        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/section/{id}")
    ResponseEntity<Response> deleteSection(@PathVariable(required = true) Long id) {
        if(!repo.existsById(id)) {
            status = HttpStatus.BAD_REQUEST;
            response = new Response("Not found todo", 400, "Error");
        } else {
            status = HttpStatus.OK;
            repo.deleteById(id);
            response = new Response("Delete section with todos",201, "Success");
        }
        return new ResponseEntity<>(response,status);
    }
}
