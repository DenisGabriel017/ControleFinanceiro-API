package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErroResponseDTO> criarResposta(HttpStatus status, String erro, String mensagem) {
        ErroResponseDTO body = new ErroResponseDTO(
                status.value(),
                erro,
                mensagem,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }


    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<ErroResponseDTO> handleBusinessException(RuntimeException e){
        return criarResposta(HttpStatus.BAD_REQUEST, "Erro de validação", e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponseDTO> handleBadCredential(BadCredentialsException e){
        return criarResposta(HttpStatus.FORBIDDEN, "Acesso Negado", "E-mail ou senha inválidos.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenericException(Exception e){
        e.printStackTrace();
        return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno", "Ocorreu um erro inesperado");
    }


}
