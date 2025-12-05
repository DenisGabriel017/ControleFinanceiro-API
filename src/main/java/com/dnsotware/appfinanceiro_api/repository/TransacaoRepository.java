package com.dnsotware.appfinanceiro_api.repository;

import com.dnsotware.appfinanceiro_api.model.Transacao;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findAllByUsuario(Usuario usuario);
}
