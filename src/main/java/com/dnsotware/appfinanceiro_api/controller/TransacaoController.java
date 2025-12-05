package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.TransacaoRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.TransacaoResponseDTO;
import com.dnsotware.appfinanceiro_api.service.TransacaoService;
import org.apache.coyote.Response;
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

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody TransacaoRequestDTO dados){
        TransacaoResponseDTO atualizado = service.atualizar(id,dados);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
