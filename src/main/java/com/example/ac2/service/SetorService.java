
package com.example.ac2.service;

import com.example.ac2.dto.*;
import com.example.ac2.model.Setor;
import com.example.ac2.repository.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class SetorService {

    private final SetorRepository setorRepository;

    public SetorService(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Transactional
    public void adicionar(SetorDTO dto) {
        if (dto == null ||
            dto.nome() == null ||
            dto.nome().isBlank()) {
            throw new IllegalArgumentException(
                "O nome do setor é obrigatório."
            );
        }

        Setor setor = new Setor();
        setor.setNome(dto.nome().trim());

        setorRepository.save(setor);
    }

    @Transactional(readOnly = true)
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        Setor setor = setorRepository.findById(idSetor)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Setor não encontrado."
            ));

        return converterSetor(setor);
    }

    @Transactional(readOnly = true)
    public List<DadosSetorDTO> listarTodos() {
        return setorRepository.findAll()
            .stream()
            .map(this::converterSetor)
            .toList();
    }

    private DadosSetorDTO converterSetor(Setor setor) {
        List<DadosFuncionarioDTO> funcionarios =
            setor.getFuncionarios().stream()
                .map(f -> new DadosFuncionarioDTO(
                    f.getId(),
                    f.getNome(),
                    f.getFuncao(),
                    setor.getId()
                ))
                .toList();

        return new DadosSetorDTO(
            setor.getId(),
            setor.getNome(),
            funcionarios
        );
    }
}
