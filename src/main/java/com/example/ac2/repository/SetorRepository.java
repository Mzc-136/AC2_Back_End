package com.example.ac2.repository;

import com.example.ac2.model.Setor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetorRepository
        extends JpaRepository<Setor, Integer> {

    @Override
    @EntityGraph(attributePaths = "funcionarios")
    List<Setor> findAll();
}
