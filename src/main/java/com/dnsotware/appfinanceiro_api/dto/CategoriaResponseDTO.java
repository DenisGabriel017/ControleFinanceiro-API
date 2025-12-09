package com.dnsotware.appfinanceiro_api.dto;

import com.dnsotware.appfinanceiro_api.model.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String tipo,
        Boolean personalizada
) {
    public CategoriaResponseDTO(Categoria categoria){
        this(
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getUsuarioId() != null
        );

    }
}
