package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.CategoriaRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.CategoriaResponseDTO;
import com.dnsotware.appfinanceiro_api.service.CategoriaService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class ControllerCategoria {

    private final CategoriaService categoriaService;
    public ControllerCategoria(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaRequestDTO dados){
        CategoriaResponseDTO novaCategoria = categoriaService.criar(dados);
        return ResponseEntity.ok(novaCategoria);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }



}
