package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.dto.RegistroDTO;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public void registrar(RegistroDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()) != null) {
            throw new RuntimeException("Usuário com esse email já existe.");
        }
        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), senhaCriptografada);
        this.usuarioRepository.save(novoUsuario);
    }

    public String solicitarCodigoReset(String email) {

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "Solicitação processada. Verifique seu e-mail.";
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));

        usuario.setCodigoResetExpiracao(LocalDateTime.now().plusMinutes(5));
        usuario.setCodigoReset(codigo);
        usuarioRepository.save(usuario);

        emailService.enviarEmailTexto(
                usuario.getEmail(),
                "Redefinição de Senha - App Financeiro",
                "Olá, " + usuario.getNome() + ".\n\nSeu código de verificação é: " + codigo + "\n\nEste código expira em 5 minutos."
        );

        return "Solicitação processada. Verifique seu e-mail.";
    }
    public Usuario validarCodigoReset(String email, String codigo) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);

        if (usuario == null) throw new IllegalArgumentException("E-mail inválido.");
        if (!codigo.equals(usuario.getCodigoReset())) throw new IllegalArgumentException("Código incorreto.");
        if (LocalDateTime.now().isAfter(usuario.getCodigoResetExpiracao())) throw new IllegalArgumentException("Código expirado.");

        return usuario;
    }

    public void resetarSenha(Usuario usuario, String novaSenha) {
        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCriptografada);
        usuario.setCodigoReset(null);
        usuario.setCodigoResetExpiracao(null);
        usuarioRepository.save(usuario);
    }
}