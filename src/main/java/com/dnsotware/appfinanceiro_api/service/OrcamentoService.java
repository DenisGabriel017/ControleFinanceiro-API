package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.OrcamentoRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.OrcamentoResponseDTO;
import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Orcamento;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.CategoriaRepository;
import com.dnsotware.appfinanceiro_api.repository.OrcamentoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.orcamentoRepository = orcamentoRepository;
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

    public OrcamentoResponseDTO definirOrcamento(OrcamentoRequestDTO dados){
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = categoriaRepository.findById(dados.categoriaId()).orElseThrow(()-> new RuntimeException("Categoria não encontrada"));

        Optional<Orcamento> existente = orcamentoRepository.findByUsuarioAndCategoria(usuario, categoria);
        Orcamento orcamento;
        if(existente.isPresent()){
            orcamento = existente.get();
            orcamento.setValor(dados.valor());
        }else {
            orcamento = new Orcamento(dados.valor(), usuario, categoria);
        }
        orcamentoRepository.save(orcamento);
        return new OrcamentoResponseDTO(orcamento);
    }

    public List<OrcamentoResponseDTO> listar(){
        Usuario usuario = getUsuarioLogado();
        return orcamentoRepository.findAllByUsuario(usuario).stream().map(OrcamentoResponseDTO::new).collect(Collectors.toList());
    }

    public void deletar(Long id){
        Usuario usuario = getUsuarioLogado();
        Orcamento orcamento =  orcamentoRepository.findById(id).orElseThrow(()-> new RuntimeException("Orçamento não encontrado"));

        if(!orcamento.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("Você não tem permissão para deletar esse orçamento.");
        }
        orcamentoRepository.delete(orcamento);
    }



}
