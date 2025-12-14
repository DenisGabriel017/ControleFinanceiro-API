package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.TransacaoRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.TransacaoResponseDTO;
import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Transacao;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.CategoriaRepository;
import com.dnsotware.appfinanceiro_api.repository.TransacaoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioLogado(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Não foi possivel identificar o usuário logado.");
    }

    public TransacaoResponseDTO criar (TransacaoRequestDTO dados){
        Usuario usuario = getUsuarioLogado();
        Categoria categoria = categoriaRepository.findById(dados.categoriaId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Transacao nova = new Transacao(
                dados.descricao(),
                dados.valor(),
                dados.data(),
                dados.tipo(),
                usuario,
                categoria
        );

        nova.setGrupo(usuario.getGrupo());

        transacaoRepository.save(nova);

        transacaoRepository.save(nova);
        return new TransacaoResponseDTO(nova);
    }

    public List<TransacaoResponseDTO> listar() {
        Usuario usuario = getUsuarioLogado();
        List<Transacao> transacoes;

        if (usuario.getGrupo() != null) {

            transacoes = transacaoRepository.findAllByGrupo(usuario.getGrupo());
        } else {

            transacoes = transacaoRepository.findAllByUsuario(usuario);
        }

        return transacoes.stream()
                .map(TransacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO dados){
        Usuario usuario = getUsuarioLogado();
        Transacao transacao = transacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        validarPermissao(usuario, transacao);

        if (!transacao.getCategoria().getId().equals(dados.categoriaId())){
            Categoria novaCategoria = categoriaRepository.findById(dados.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            transacao.setCategoria(novaCategoria);
        }

        transacao.setDescricao(dados.descricao());
        transacao.setValor(dados.valor());
        transacao.setData(dados.data());
        transacao.setTipo(dados.tipo());

        transacaoRepository.save(transacao);
        return new TransacaoResponseDTO(transacao);
    }

    public void deletar(Long id){
        Usuario usuario = getUsuarioLogado();
        Transacao transacao = transacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        validarPermissao(usuario, transacao);

        transacaoRepository.delete(transacao);
    }

    public Transacao salvarSistema(Transacao transacao) {
        transacaoRepository.save(transacao);
        return transacao;
    }

    private void validarPermissao(Usuario usuarioLogado, Transacao transacao) {

        boolean ehDono = transacao.getUsuario().getId().equals(usuarioLogado.getId());

        boolean mesmoGrupo = false;
        if (usuarioLogado.getGrupo() != null && transacao.getGrupo() != null) {
            mesmoGrupo = usuarioLogado.getGrupo().getId().equals(transacao.getGrupo().getId());
        }

        if (!ehDono && !mesmoGrupo) {
            throw new RuntimeException("Você não tem permissão para alterar/deletar este registro.");
        }
    }
}