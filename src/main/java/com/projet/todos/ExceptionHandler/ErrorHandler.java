package com.projet.todos.ExceptionHandler;

import com.projet.todos.models.Response;
import org.hibernate.PropertyValueException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    Response result;

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<Response> nullPointerException(RuntimeException e) {
        result = new Response("[Exception Response] - Exception: " + e.getMessage(), 400, "Error");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Response> noExistClass(RuntimeException e) {
        result = new Response("[Exception Response] - Exception: " + e.getMessage(), 400, "Error");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
