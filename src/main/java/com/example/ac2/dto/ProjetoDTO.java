package com.example.ac2.dto;

import java.time.LocalDate;

public record ProjetoDTO(
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim
) {
}
