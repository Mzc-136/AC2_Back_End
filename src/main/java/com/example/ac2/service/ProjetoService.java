
package com.example.ac2.service;

import com.example.ac2.dto.*;
import com.example.ac2.model.*;
import com.example.ac2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ProjetoService(
            ProjetoRepository projetoRepository,
            FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional
    public void adicionar(ProjetoDTO dto) {
        if (dto == null ||
            dto.descricao() == null ||
            dto.descricao().isBlank()) {
            throw new IllegalArgumentException(
                "A descrição do projeto é obrigatória."
            );
        }

        validarDatas(dto.dataInicio(), dto.dataFim());

        Projeto projeto = new Projeto();
        projeto.setDescricao(dto.descricao().trim());
        projeto.setDataInicio(dto.dataInicio());
        projeto.setDataFim(dto.dataFim());

        projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        Projeto projeto = projetoRepository
            .buscarComFuncionarios(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Projeto não encontrado."
            ));

        return converterProjeto(projeto);
    }

    @Transactional
    public void vincularFuncionario(
            Integer idProjeto, Integer idFuncionario) {

        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Projeto não encontrado."
            ));

        Funcionario funcionario = funcionarioRepository
            .findById(idFuncionario)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Funcionário não encontrado."
            ));

        projeto.adicionarFuncionario(funcionario);
        projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> listarTodos() {
        return projetoRepository.findAll()
            .stream()
            .map(this::converterProjeto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarPorPeriodo(
            LocalDate inicio, LocalDate fim) {

        validarDatas(inicio, fim);

        return projetoRepository.findByDataInicioBetween(inicio, fim)
            .stream()
            .map(this::converterProjeto)
            .toList();
    }

    private void validarDatas(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException(
                "As datas de início e fim são obrigatórias."
            );
        }

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException(
                "A data final não pode ser anterior à inicial."
            );
        }
    }

    private DadosProjetoDTO converterProjeto(Projeto projeto) {
        List<DadosFuncionarioDTO> funcionarios =
            projeto.getFuncionarios().stream()
                .map(f -> new DadosFuncionarioDTO(
                    f.getId(),
                    f.getNome(),
                    f.getFuncao(),
                    f.getSetor().getId()
                ))
                .toList();

        return new DadosProjetoDTO(
            projeto.getId(),
            projeto.getDescricao(),
            projeto.getDataInicio(),
            projeto.getDataFim(),
            funcionarios
        );
    }
}
