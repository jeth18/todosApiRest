package com.projet.todos.Repository;

import com.projet.todos.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByIdUser(Long id);

}
