package com.example.ac2.repository;

import com.example.ac2.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepository
        extends JpaRepository<Projeto, Integer> {

    @EntityGraph(attributePaths = "funcionarios")
    @Query("SELECT p FROM Projeto p WHERE p.id = :id")
    Optional<Projeto> buscarComFuncionarios(
        @Param("id") Integer id
    );

    List<Projeto> findByDataInicioBetween(
        LocalDate inicio,
        LocalDate fim
    );
}
