package com.dnsotware.appfinanceiro_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.aot.generate.Generated;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "usuarioId")
    private Long usuarioId;

    public Categoria(String nome, String tipo, Long usuarioId){
        this.nome = nome;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
    }
}
