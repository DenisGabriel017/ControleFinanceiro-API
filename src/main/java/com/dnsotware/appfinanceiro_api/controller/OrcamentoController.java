package com.dnsotware.appfinanceiro_api.controller;


import com.dnsotware.appfinanceiro_api.dto.OrcamentoRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.OrcamentoResponseDTO;
import com.dnsotware.appfinanceiro_api.service.OrcamentoService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponseDTO> definir(@RequestBody OrcamentoRequestDTO dados){
        return ResponseEntity.ok(service.definirOrcamento(dados));

    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar (@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
