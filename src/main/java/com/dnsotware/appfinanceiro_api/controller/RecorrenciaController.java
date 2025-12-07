package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.RecorrenciaRequestDTO;
import com.dnsotware.appfinanceiro_api.model.Recorrencia;
import com.dnsotware.appfinanceiro_api.service.RecorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recorrencias")
public class RecorrenciaController {

    private final RecorrenciaService service;

    public RecorrenciaController(RecorrenciaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Recorrencia> criar(@RequestBody RecorrenciaRequestDTO dados) {
        return ResponseEntity.ok(service.criar(dados));
    }
}