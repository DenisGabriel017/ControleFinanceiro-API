package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.GrupoRequestDTO;
import com.dnsotware.appfinanceiro_api.model.Grupo;
import com.dnsotware.appfinanceiro_api.service.GrupoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService service;

    public GrupoController(GrupoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Grupo> criar(@RequestBody GrupoRequestDTO dados) {
        var novoGrupo = service.criar(dados);
        return ResponseEntity.ok(novoGrupo);
    }
}