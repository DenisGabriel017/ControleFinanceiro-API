package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.model.Transacao;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.TransacaoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public TransacaoService(TransacaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = authentication.getName(); // Agora é seguro chamar o .getName()
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco"));
    }

    public Transacao salvar(Transacao transacao) {
        Usuario usuario = getUsuarioLogado();
        transacao.setUsuario(usuario); // Associa a transação ao usuário logado
        return repository.save(transacao);
    }

    public List<Transacao> listarMinhas() {
        Usuario usuario = getUsuarioLogado();
        return repository.findAllByUsuario(usuario); // Filtra só as dele
    }
}