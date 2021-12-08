package com.projet.todos.Controllers;
import javax.validation.Valid;
import com.projet.todos.Repository.ISectionRepository;
import com.projet.todos.models.Response;
import com.projet.todos.models.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

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
        response = new Response(repo.save(section), 200, "Success");
        log.info("Section save: "+ response.getData());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
