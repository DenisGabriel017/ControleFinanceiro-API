package com.dnsotware.appfinanceiro_api.dto;

import com.dnsotware.appfinanceiro_api.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponseDTO(Long id, String descricao, BigDecimal valor, LocalDate data, String tipo, String nomeCategoria, Long categoriaId) {

    public TransacaoResponseDTO(Transacao transacao) {
        this(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo(),
                transacao.getCategoria() != null ? transacao.getCategoria().getNome() : "Sem Categoria",
                transacao.getCategoria() != null ? transacao.getCategoria().getId() : null
        );
    }
}
