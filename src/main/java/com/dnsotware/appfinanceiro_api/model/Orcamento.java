package com.dnsotware.appfinanceiro_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;

@Entity
@Table(name = "orcamentos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "categoria_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Orcamento(BigDecimal valor, Usuario usuario, Categoria categoria){
        this.valor = valor;
        this.usuario = usuario;
        this.categoria = categoria;
    }

}
