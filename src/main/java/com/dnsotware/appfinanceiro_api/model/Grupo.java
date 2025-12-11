package com.dnsotware.appfinanceiro_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String codigoAcesso;

    private LocalDateTime validadeCodigo;

    @OneToOne
    @JoinColumn(name = "criador_id")
    @JsonIgnoreProperties({"grupo", "senha", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    private Usuario criador;

    @OneToMany(mappedBy = "grupo")
    @JsonIgnore
    private List<Usuario> membros = new ArrayList<>();

    public Grupo() {}

    public Grupo(String nome, Usuario criador) {
        this.nome = nome;
        this.criador = criador;
    }

    public void setId(Long id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    public void setCodigoAcesso(String codigoAcesso) { this.codigoAcesso = codigoAcesso; }

    public void setValidadeCodigo(LocalDateTime validadeCodigo) { this.validadeCodigo = validadeCodigo; }

    public void setCriador(Usuario criador) { this.criador = criador; }

    public void setMembros(List<Usuario> membros) { this.membros = membros; }
}