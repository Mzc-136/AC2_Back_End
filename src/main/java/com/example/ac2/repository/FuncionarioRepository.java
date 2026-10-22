package com.example.ac2.repository;

import com.example.ac2.model.Funcionario;
import com.example.ac2.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FuncionarioRepository
        extends JpaRepository<Funcionario, Integer> {

    @Query("""
        SELECT DISTINCT p
        FROM Funcionario f
        JOIN f.projetos p
        WHERE f.id = :id
    """)
    List<Projeto> buscarProjetos(
        @Param("id") Integer id
    );
}
