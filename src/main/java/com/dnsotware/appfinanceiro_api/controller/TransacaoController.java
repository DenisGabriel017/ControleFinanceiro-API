package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.TransacaoRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.TransacaoResponseDTO;
import com.dnsotware.appfinanceiro_api.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService service;

    public TransacaoController(TransacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar (@RequestBody TransacaoRequestDTO dados){
        TransacaoResponseDTO novaTransacao = service.criar(dados);
        return ResponseEntity.ok(novaTransacao);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listar(){
        List<TransacaoResponseDTO> lista = service.listar();
        return ResponseEntity.ok(lista);
    }
}
