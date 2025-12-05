package com.dnsotware.appfinanceiro_api.repository;

import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Orcamento;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    List<Orcamento> findAllByUsuario(Usuario usuario);

    Optional<Orcamento> findByUsuarioAndCategoria(Usuario usuario, Categoria categoria);
}
