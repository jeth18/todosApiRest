package com.projet.todos.Controllers;

import com.projet.todos.Repository.IUserRepository;
import com.projet.todos.models.Response;
import com.projet.todos.models.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor @Transactional @Slf4j
public class UserController implements UserDetailsService {

    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private IUserRepository repo;

    private final PasswordEncoder passwordEncoder;

    private Response response;

    @PostMapping("/users")
    ResponseEntity<Response> saveUsuario(@RequestBody Users user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        response = new Response(repo.save(user), 200, "Success");
        log.info("Save user: "+ response.getData());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/users")
    ResponseEntity<Response> getAllUsuarios() {
        response = new Response(repo.findAll(), 200, "Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/roles/addtousers")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizarionHeader = request.getHeader(AUTHORIZATION);

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);
        if(user == null) {
            log.info("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

}
