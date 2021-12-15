package com.projet.todos.Repository;

import com.projet.todos.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}