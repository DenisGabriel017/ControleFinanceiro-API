package com.dnsotware.appfinanceiro_api.repository;

import com.dnsotware.appfinanceiro_api.model.Grupo;
import com.dnsotware.appfinanceiro_api.model.Transacao;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findAllByUsuario(Usuario usuario);
    List<Transacao> findByUsuario_Grupo(Grupo grupo);

    @Query("SELECT COALESCE(SUM(t.valor),0) FROM Transacao t WHERE t.usuario = :usuario AND t.tipo = :tipo")
    BigDecimal somarPorTipo(@Param("usuario") Usuario usuario, @Param("tipo") String tipo);

    @Query("SELECT t.categoria, SUM(t.valor) FROM Transacao t " + "WHERE t.usuario = :usuario AND t.tipo = 'DESPESA' " + "GROUP BY t.categoria")
    List<Object[]>somarDespesasPorCategoria(@Param("usuario") Usuario usuario);

}
