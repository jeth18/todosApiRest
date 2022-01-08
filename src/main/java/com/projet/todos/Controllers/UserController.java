package com.projet.todos.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.IOException;
import java.util.*;

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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizarionHeader = request.getHeader(AUTHORIZATION);
        if(authorizarionHeader != null && authorizarionHeader.startsWith("Bearer ")) {
            try{
                String refresh_token = authorizarionHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Users user = repo.findByUsername(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("Error", e.getMessage());
                response.setStatus(403);
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message",e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
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
