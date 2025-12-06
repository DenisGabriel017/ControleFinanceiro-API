package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.DashboardCategoriaDTO;
import com.dnsotware.appfinanceiro_api.dto.DashboardResponseDTO;
import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Orcamento;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.OrcamentoRepository;
import com.dnsotware.appfinanceiro_api.repository.TransacaoRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrcamentoRepository orcamentoRepository;

    public DashboardService(TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository, OrcamentoRepository orcamentoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.orcamentoRepository = orcamentoRepository;
    }

    private Usuario getUsuarioLogado(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario){
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Não foi possivel identificar o usuário logado.");
    }

    public DashboardResponseDTO carregarDashboard(){
        Usuario usuario = getUsuarioLogado();

        BigDecimal resceitas = transacaoRepository.somarPorTipo(usuario,"RECEITA");
        BigDecimal despesas = transacaoRepository.somarPorTipo(usuario,"DESPESA");

        BigDecimal saldo = resceitas.subtract(despesas);

        return new DashboardResponseDTO(resceitas, despesas, saldo);
    }

    public List<DashboardCategoriaDTO> carregarDetalhamento(){
        Usuario usuario = getUsuarioLogado();
        List<Orcamento> orcamentos = orcamentoRepository.findAllByUsuario(usuario);
        List<Object[]> gastosPorCategoria = transacaoRepository.somarDespesasPorCategoria(usuario);
        Map<Long, DashboardCategoriaDTO> mapResultados = new HashMap<>();

        for (Object[] row : gastosPorCategoria){
            Categoria cat = (Categoria) row[0];
            BigDecimal totalGasto = (BigDecimal) row[1];

            mapResultados.put(cat.getId(),new DashboardCategoriaDTO(
                    cat.getNome(), BigDecimal.ZERO, totalGasto, BigDecimal.ZERO.subtract(totalGasto))
            );
        }
        for (Orcamento orc : orcamentos){
            Long catId = orc.getCategoria().getId();
            BigDecimal limite = orc.getValor();

            if (mapResultados.containsKey(catId)) {
                DashboardCategoriaDTO existente = mapResultados.get(catId);
                mapResultados.put(catId, new DashboardCategoriaDTO(
                        existente.nomeCategoria(),
                        limite,
                        existente.gasto(),
                        limite.subtract(existente.gasto())
                ));
            }else {
                mapResultados.put(catId, new DashboardCategoriaDTO(
                        orc.getCategoria().getNome(),
                        limite,
                        BigDecimal.ZERO,
                        limite
                ));
            }
        }
        return new ArrayList<>(mapResultados.values());
    }
}
