package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.GrupoRequestDTO;
import com.dnsotware.appfinanceiro_api.model.Grupo;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.GrupoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    public GrupoService(GrupoRepository grupoRepository, UsuarioRepository usuarioRepository) {
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Grupo criar(GrupoRequestDTO dados) {

        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.getGrupo() != null) {
            throw new RuntimeException("Você já participa de um grupo! Saia dele antes de criar outro.");
        }

        Grupo novoGrupo = new Grupo();
        novoGrupo.setNome(dados.nome());
        novoGrupo.setCriador(usuarioLogado);

        grupoRepository.save(novoGrupo);

        usuarioLogado.setGrupo(novoGrupo);
        usuarioRepository.save(usuarioLogado);

        return novoGrupo;
    }

    private Usuario getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuário não identificado");
    }
}