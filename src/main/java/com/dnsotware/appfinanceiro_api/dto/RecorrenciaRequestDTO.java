package com.dnsotware.appfinanceiro_api.dto;

import com.dnsotware.appfinanceiro_api.model.Frequencia;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RecorrenciaRequestDTO(
        String descricao,
        BigDecimal valor,
        String tipo, // RECEITA ou DESPESA
        LocalDate dataInicio,
        Frequencia frequencia // MENSAL, SEMANAL...
) {}