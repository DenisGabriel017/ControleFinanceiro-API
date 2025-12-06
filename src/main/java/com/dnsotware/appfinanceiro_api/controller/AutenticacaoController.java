package com.dnsotware.appfinanceiro_api.controller;

import com.dnsotware.appfinanceiro_api.dto.AutenticacaoDTO;
import com.dnsotware.appfinanceiro_api.dto.LoginResponseDTO;
import com.dnsotware.appfinanceiro_api.dto.RegistroDTO;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import com.dnsotware.appfinanceiro_api.service.TokenService;
import com.dnsotware.appfinanceiro_api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final UsuarioRepository repository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    record EmailDTO(String email){}
    record ResetCodeDTO(String email,String codigo){}
    record ResetSenhaDTO(String email, String codigo, String novaSenha){}


    public AutenticacaoController(UsuarioRepository repository, TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsuarioService usuarioService)
    {
        this.repository = repository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistroDTO data) {
        try {
            usuarioService.registrar(data);
            return ResponseEntity.ok("Usúario cadastrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AutenticacaoDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/solicitar-codigo")
    public ResponseEntity<String> solicitarCodigo(@RequestBody EmailDTO data){
        String mensagem = usuarioService.solicitarCodigoReset(data.email());
        return ResponseEntity.ok(mensagem);

    }
    @PostMapping("/validar-codigo")
    public ResponseEntity<String> validarCodigo(@RequestBody ResetCodeDTO data){
        usuarioService.validarCodigoReset(data.email(), data.codigo());

        return ResponseEntity.ok("Código validado com sucesso.");
    }
    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestBody ResetSenhaDTO data){
        Usuario usuario = usuarioService.validarCodigoReset(data.email(), data.codigo());
        usuarioService.resetarSenha(usuario, data.novaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }




}
