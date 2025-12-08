package com.dnsotware.appfinanceiro_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "grupos")
public class Grupo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToOne
    @JoinColumn(name = "criador_id")
    private Usuario criador;

    @OneToMany(mappedBy = "grupo")
    @JsonIgnore
    private List<Usuario> membros = new ArrayList<>();

    public Grupo() {}

    public Grupo(String nome, Usuario criador) {
        this.nome = nome;
        this.criador = criador;
    }

}