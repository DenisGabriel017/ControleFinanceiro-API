package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.GrupoRequestDTO;
import com.dnsotware.appfinanceiro_api.model.Grupo;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.dto.GrupoDetalhadoDTO;
import java.util.stream.Collectors;
import com.dnsotware.appfinanceiro_api.repository.GrupoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
            throw new RuntimeException("Você já participa de um grupo!");
        }

        Grupo novoGrupo = new Grupo();
        novoGrupo.setNome(dados.nome());
        novoGrupo.setCriador(usuarioLogado);

        String codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        novoGrupo.setCodigoAcesso(codigo);

        novoGrupo.setValidadeCodigo(LocalDateTime.now().plusHours(24));

        grupoRepository.save(novoGrupo);

        usuarioLogado.setGrupo(novoGrupo);
        usuarioRepository.save(usuarioLogado);

        return novoGrupo;
    }

    public Grupo entrar(String codigoAcesso) {
        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.getGrupo() != null) {
            throw new RuntimeException("Você já está em um grupo! Saia antes de entrar em outro.");
        }

        Grupo grupo = grupoRepository.findByCodigoAcesso(codigoAcesso)
                .orElseThrow(() -> new RuntimeException("Código de grupo inválido ou inexistente."));

        if (grupo.getValidadeCodigo() != null && LocalDateTime.now().isAfter(grupo.getValidadeCodigo())) {
            throw new RuntimeException("Este código de convite expirou! Peça para o dono gerar um novo.");
        }

        usuarioLogado.setGrupo(grupo);
        usuarioRepository.save(usuarioLogado);

        return grupo;
    }

    private Usuario getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuário não identificado");
    }

    public void sair() {
        Usuario usuario = getUsuarioLogado();

        if (usuario.getGrupo() == null) {
            throw new RuntimeException("Você não participa de nenhum grupo!");
        }

        usuario.setGrupo(null);
        usuarioRepository.save(usuario);
    }

    public void removerMembro(Long idMembroParaRemover) {
        Usuario dono = getUsuarioLogado();
        Grupo grupo = dono.getGrupo();

        if (grupo == null) {
            throw new RuntimeException("Você não é dono de nenhum grupo.");
        }

        if (!grupo.getCriador().getId().equals(dono.getId())) {
            throw new RuntimeException("Apenas o criador do grupo pode remover membros.");
        }

        Usuario vitima = usuarioRepository.findById(idMembroParaRemover)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (vitima.getGrupo() == null || !vitima.getGrupo().getId().equals(grupo.getId())) {
            throw new RuntimeException("Este usuário não pertence ao seu grupo.");
        }

        if (vitima.getId().equals(dono.getId())) {
            throw new RuntimeException("Você não pode se expulsar. Use a opção 'Sair do Grupo'.");
        }

        vitima.setGrupo(null);
        usuarioRepository.save(vitima);
    }

    public GrupoDetalhadoDTO buscarMeuGrupo() {
        Usuario usuarioLogado = getUsuarioLogado();
        Grupo grupo = usuarioLogado.getGrupo();

        if (grupo == null) {
            return null; // O usuário não tem grupo
        }

        List<Usuario> membros = usuarioRepository.findAllByGrupo(grupo);

        List<GrupoDetalhadoDTO.MembroDTO> membrosDTO = membros.stream()
                .map(m -> new GrupoDetalhadoDTO.MembroDTO(m.getId(), m.getNome(), m.getEmail()))
                .collect(Collectors.toList());

        return new GrupoDetalhadoDTO(
                grupo.getId(),
                grupo.getNome(),
                grupo.getCodigoAcesso(),
                grupo.getValidadeCodigo(),
                grupo.getCriador().getId(),
                grupo.getCriador().getNome(),
                membrosDTO
        );
    }

    public String gerarNovoCodigo() {
        Usuario usuario = getUsuarioLogado();
        Grupo grupo = usuario.getGrupo();

        if (grupo == null) {
            throw new RuntimeException("Você não possui um grupo.");
        }

        if (!grupo.getCriador().getId().equals(usuario.getId())) {
            throw new RuntimeException("Apenas o criador do grupo pode gerar novos códigos.");
        }

        String novoCodigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        grupo.setCodigoAcesso(novoCodigo);
        grupo.setValidadeCodigo(LocalDateTime.now().plusHours(24));
        grupoRepository.save(grupo);

        return novoCodigo;
    }

    @org.springframework.transaction.annotation.Transactional // Garante que tudo aconteça ou nada aconteça
    public void deletarGrupo() {
        Usuario dono = getUsuarioLogado();
        Grupo grupo = dono.getGrupo();

        if (grupo == null) {
            throw new RuntimeException("Você não possui um grupo para excluir.");
        }

        if (!grupo.getCriador().getId().equals(dono.getId())) {
            throw new RuntimeException("Apenas o criador pode excluir o grupo.");
        }

        List<Usuario> membros = usuarioRepository.findAllByGrupo(grupo);

        for (Usuario membro : membros) {
            membro.setGrupo(null);
        }

        usuarioRepository.saveAll(membros);

        grupoRepository.delete(grupo);
    }
}