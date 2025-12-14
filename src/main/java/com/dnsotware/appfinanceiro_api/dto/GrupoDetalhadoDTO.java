package com.dnsotware.appfinanceiro_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GrupoDetalhadoDTO(
        Long id,
        String nome,
        String codigoAcesso,
        LocalDateTime validadeCodigo,
        Long idCriador,
        String nomeCriador,
        List<MembroDTO> membros
) {

    public record MembroDTO(Long id, String nome, String email) {}
}