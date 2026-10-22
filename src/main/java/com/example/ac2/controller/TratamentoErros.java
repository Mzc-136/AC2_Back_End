package com.example.ac2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class TratamentoErros {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
            Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> tratarNaoEncontrado(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(
            ex.getStatusCode().value()
        );

        return ResponseEntity.status(status).body(
            Map.of("erro", ex.getReason() == null
                ? "Não foi possível processar a solicitação."
                : ex.getReason())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> tratarErroInterno(
            Exception ex) {

        return ResponseEntity.status(
            HttpStatus.INTERNAL_SERVER_ERROR
        ).body(Map.of(
            "erro", "Ocorreu um erro interno. Tente novamente."
        ));
    }
}
