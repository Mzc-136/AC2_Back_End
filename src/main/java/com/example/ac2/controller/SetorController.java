package com.example.ac2.controller;

import com.example.ac2.dto.*;
import com.example.ac2.service.SetorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setores")
public class SetorController {

    private final SetorService service;

    public SetorController(SetorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody SetorDTO setor) {
        service.adicionar(setor);
    }

    @GetMapping("/{idSetor}")
    public DadosSetorDTO buscarSetorPorId(
            @PathVariable Integer idSetor) {
        return service.buscarSetorPorId(idSetor);
    }

    @GetMapping
    public List<DadosSetorDTO> listarTodos() {
        return service.listarTodos();
    }
}
