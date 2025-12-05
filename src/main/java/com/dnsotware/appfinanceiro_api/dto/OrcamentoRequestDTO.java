package com.dnsotware.appfinanceiro_api.dto;

import java.math.BigDecimal;

public record OrcamentoRequestDTO(
        BigDecimal valor,
        Long categoriaId
) {
}
