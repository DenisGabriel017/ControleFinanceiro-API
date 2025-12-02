package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.AutenticacaoDTO;
import com.dnsotware.appfinanceiro_api.dto.LoginResponseDTO;
import com.dnsotware.appfinanceiro_api.dto.RegistroDTO;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import com.dnsotware.appfinanceiro_api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistroDTO data) {
        if (this.repository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("Usuário com esse email ja existe.");
        }
        String senhaCriptografada = passwordEncoder.encode(data.senha());

        Usuario novoUsuario = new Usuario(data.nome(), data.email(), senhaCriptografada);
        this.repository.save(novoUsuario);
        return ResponseEntity.ok("Usúario cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AutenticacaoDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }catch (AuthenticationException e){
            System.err.println("Falha na autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
