package com.dnsotware.appfinanceiro_api.dto;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal totalReceitas,
        BigDecimal totaDespesas,
        BigDecimal saldo
) {
}
