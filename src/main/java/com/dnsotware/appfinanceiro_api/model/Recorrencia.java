package com.dnsotware.appfinanceiro_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "recorrencias")
public class Recorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "proxima_execucao")
    private LocalDate proximaExecucao;

    @Enumerated(EnumType.STRING)
    private Frequencia frequencia;

    private boolean ativa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Recorrencia() {
        this.ativa = true;
    }

}