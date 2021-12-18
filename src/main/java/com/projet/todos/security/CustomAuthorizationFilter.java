package com.projet.todos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizarionHeader = request.getHeader(AUTHORIZATION);
            if(authorizarionHeader != null && authorizarionHeader.startsWith("Bearer ")) {
                try{
                    String token = authorizarionHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("Error loggin in: {}", e.getMessage());
                    response.setHeader("Error", e.getMessage());
                    response.setStatus(403);
                    Map<String, String> errors = new HashMap<>();
                    errors.put("error_message",e.getMessage());
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
