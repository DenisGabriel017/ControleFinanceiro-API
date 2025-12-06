package com.dnsotware.appfinanceiro_api.dto;

import java.math.BigDecimal;

public record DashboardCategoriaDTO(
        String nomeCategoria,
        BigDecimal orcado,
        BigDecimal gasto,
        BigDecimal restante
) {
}
