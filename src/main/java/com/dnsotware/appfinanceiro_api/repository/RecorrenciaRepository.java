package com.dnsotware.appfinanceiro_api.repository;

import com.dnsotware.appfinanceiro_api.model.Recorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface RecorrenciaRepository extends JpaRepository<Recorrencia, Long> {

    List<Recorrencia> findAllByProximaExecucaoLessThanEqualAndAtivaTrue(LocalDate data);
}