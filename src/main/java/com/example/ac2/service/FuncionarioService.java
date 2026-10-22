
package com.example.ac2.service;

import com.example.ac2.dto.*;
import com.example.ac2.model.*;
import com.example.ac2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            SetorRepository setorRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
    }

    @Transactional
    public void adicionar(FuncionarioDTO dto) {
        if (dto == null ||
            dto.nome() == null || dto.nome().isBlank() ||
            dto.funcao() == null || dto.funcao().isBlank()) {
            throw new IllegalArgumentException(
                "Nome e função do funcionário são obrigatórios."
            );
        }

        if (dto.idSetor() == null) {
            throw new IllegalArgumentException(
                "O ID do setor é obrigatório."
            );
        }

        Setor setor = setorRepository.findById(dto.idSetor())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Setor não encontrado."
            ));

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.nome().trim());
        funcionario.setFuncao(dto.funcao().trim());
        funcionario.setSetor(setor);

        funcionarioRepository.save(funcionario);
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (idFuncionario == null) {
            throw new IllegalArgumentException(
                "O ID do funcionário é obrigatório."
            );
        }

        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Funcionário não encontrado."
            );
        }

        return funcionarioRepository.buscarProjetos(idFuncionario)
            .stream()
            .map(p -> new DadosProjetoDTO(
                p.getId(),
                p.getDescricao(),
                p.getDataInicio(),
                p.getDataFim(),
                p.getFuncionarios().stream()
                    .map(f -> new DadosFuncionarioDTO(
                        f.getId(),
                        f.getNome(),
                        f.getFuncao(),
                        f.getSetor().getId()
                    ))
                    .toList()
            ))
            .toList();
    }
}
