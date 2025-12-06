package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.DashboardCategoriaDTO;
import com.dnsotware.appfinanceiro_api.dto.DashboardResponseDTO;
import com.dnsotware.appfinanceiro_api.service.DashboardService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(){
        return ResponseEntity.ok(service.carregarDashboard());
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<DashboardCategoriaDTO>> getDetalhamento(){
        return ResponseEntity.ok(service.carregarDetalhamento());
    }

}
