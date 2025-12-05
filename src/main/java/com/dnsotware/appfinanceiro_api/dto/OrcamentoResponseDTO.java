package com.dnsotware.appfinanceiro_api.dto;

import com.dnsotware.appfinanceiro_api.model.Orcamento;

import java.math.BigDecimal;

public record OrcamentoResponseDTO(Long id, BigDecimal valor,String nomeCategoria,Long categoriaId) {

    public OrcamentoResponseDTO(Orcamento orcamento) {
        this(
                orcamento.getId(),
                orcamento.getValor(),
                orcamento.getCategoria().getNome(),
                orcamento.getCategoria().getId()
        );
    }
}
