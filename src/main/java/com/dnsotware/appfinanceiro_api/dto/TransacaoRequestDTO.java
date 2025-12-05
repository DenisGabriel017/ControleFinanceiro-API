package com.dnsotware.appfinanceiro_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(String descricao, BigDecimal valor, LocalDate data, String tipo, Long categoriaId) {
}
