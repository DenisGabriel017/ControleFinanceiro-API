package com.dnsotware.appfinanceiro_api.repository;

import com.dnsotware.appfinanceiro_api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c WHERE c.usuarioId IS NULL OR c.usuarioId = :usuarioId")
    List<Categoria> findAllPadraoEUsuario(@Param("usuarioId") Long usuarioId);

}
