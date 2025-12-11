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

    @PostMapping("/entrar")
    public ResponseEntity<Grupo> entrar(@RequestParam String codigo) {
        var grupo = service.entrar(codigo);
        return ResponseEntity.ok(grupo);
    }

    @DeleteMapping("/sair")
    public ResponseEntity<Void> sair() {
        service.sair();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/membros/{id}")
    public ResponseEntity<Void> removerMembro(@PathVariable Long id) {
        service.removerMembro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/codigo-acesso")
    public ResponseEntity<String> gerarNovoCodigo() {
        String novoCodigo = service.gerarNovoCodigo();
        return ResponseEntity.ok(novoCodigo);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarGrupo() {
        service.deletarGrupo();
        return ResponseEntity.noContent().build();
    }
}