package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.CategoriaRequestDTO;
import com.dnsotware.appfinanceiro_api.dto.CategoriaResponseDTO;
import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.CategoriaRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Long getUsuarioId(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            String email = (String) authentication.getPrincipal();
            Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
            return usuario.getId();
        }
        throw new RuntimeException("Não foi possivel identificar o usuário logado.");
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dados){
        Long usuarioId = getUsuarioId();

        Categoria nova =  new Categoria(
                dados.nome(),
                dados.tipo(),
                usuarioId
        );

        categoriaRepository.save(nova);
        return new CategoriaResponseDTO(nova);
    }

    public List<CategoriaResponseDTO> listar(){
        Long usuarioId = getUsuarioId();
        List<Categoria> categorias = categoriaRepository.findAllPadraoEUsuario(usuarioId);

        return categorias.stream().map(CategoriaResponseDTO::new).collect(Collectors.toList());
    }

    public void deletar (Long id){
        Long usuarioId = getUsuarioId();
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (categoria.getUsuarioId() == null || categoria.getUsuarioId().equals(usuarioId)){
            throw new RuntimeException("Você tem permissão para deletar essa categoria.");
        }
        categoriaRepository.delete(categoria);
    }
}
