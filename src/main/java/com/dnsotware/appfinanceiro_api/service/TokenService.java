package com.dnsotware.appfinanceiro_api.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create().withIssuer("controle-financeiro-api")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("tenantId", usuario.getTenantId()) // Adicionando o tenantId
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("controle-financeiro-api")
                    .build()
                    .verify(token)
                    .getSubject();
            }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao validar token jwt", exception);
        }
    }

    public String getTenantId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("controle-financeiro-api")
                    .build()
                    .verify(token)
                    .getClaim("tenantId").asString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao validar token jwt", exception);
        }
    }

    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
