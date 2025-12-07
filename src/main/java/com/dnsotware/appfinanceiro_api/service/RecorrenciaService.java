package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.RecorrenciaRequestDTO;
import com.dnsotware.appfinanceiro_api.model.Categoria;
import com.dnsotware.appfinanceiro_api.model.Recorrencia;
import com.dnsotware.appfinanceiro_api.model.Transacao;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.CategoriaRepository;
import com.dnsotware.appfinanceiro_api.repository.RecorrenciaRepository;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecorrenciaService {

    @Autowired private RecorrenciaRepository repository;
    @Autowired private TransacaoService transacaoService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;

    private Usuario getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        throw new RuntimeException("Usuário não identificado");
    }

    public Recorrencia criar(RecorrenciaRequestDTO dados) {
        Usuario usuario = getUsuarioLogado();

        Recorrencia nova = new Recorrencia();
        nova.setDescricao(dados.descricao());
        nova.setValor(dados.valor());
        nova.setTipo(dados.tipo());
        nova.setDataInicio(dados.dataInicio());
        nova.setFrequencia(dados.frequencia());
        nova.setUsuario(usuario);
        nova.setProximaExecucao(dados.dataInicio());

        return repository.save(nova);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void processarRecorrencias() {
        LocalDate hoje = LocalDate.now();
        List<Recorrencia> aProcessar = repository.findAllByProximaExecucaoLessThanEqualAndAtivaTrue(hoje);

        Optional<Categoria> categoriaPadrao = categoriaRepository.findById(1L);

        if (categoriaPadrao.isEmpty()) {
            System.out.println("ERRO: O Robô não achou a Categoria de ID 1 para usar.");
            return;
        }

        for (Recorrencia rec : aProcessar) {
            Transacao nova = new Transacao();
            nova.setDescricao(rec.getDescricao() + " (Recorrente)");
            nova.setValor(rec.getValor());
            nova.setUsuario(rec.getUsuario());
            nova.setData(hoje);
            nova.setTipo(rec.getTipo());

            nova.setCategoria(categoriaPadrao.get());

            transacaoService.salvarSistema(nova);

            if (rec.getFrequencia().name().equals("MENSAL")) {
                rec.setProximaExecucao(rec.getProximaExecucao().plusMonths(1));
            } else if (rec.getFrequencia().name().equals("SEMANAL")) {
                rec.setProximaExecucao(rec.getProximaExecucao().plusWeeks(1));
            } else {
                rec.setProximaExecucao(rec.getProximaExecucao().plusYears(1));
            }

            repository.save(rec);
            System.out.println("SUCESSO: Recorrência processada: " + nova.getDescricao());
        }
    }
}