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

    public Usuario registrar(RegistroDTO data) {
        if (usuarioRepository.findByEmail(data.email()) != null) {
            throw new RuntimeException("Usuário com esse email ja existe.");
        }
        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), senhaCriptografada);
        return usuarioRepository.save(novoUsuario);
    }

    public String solicitarCodigoReset(String email) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return "Solicitação processada. Se o e-mail estiver cadastrado, você receberá as instruções.";
        }


        String codigo = String.format("%06d", new Random().nextInt(999999));
        usuario.setCodigoReset(codigo);
        usuario.setCodigoResetExpiracao(LocalDateTime.now().plusMinutes(5));

        usuarioRepository.save(usuario);


        emailService.enviarEmailTexto(
                usuario.getEmail(),
                "Código de Redefinição de Senha",
                "Olá, " + usuario.getNome() + "!\n\nSeu código para redefinir a senha é: " + codigo + "\n\nEste código expira em 5 minutos."
        );

        return "Solicitação processada. Verifique seu e-mail.";
    }

    public Usuario validarCodigoReset(String email, String codigo) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException("E-mail ou código inválido");
        }
        if (!codigo.equals(usuario.getCodigoReset())) {
            throw new IllegalArgumentException("Código incorreto.");
        }
        if (LocalDateTime.now().isAfter(usuario.getCodigoResetExpiracao())) {
            throw new IllegalArgumentException("Código expirado. Solicite um novo");
        }

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
