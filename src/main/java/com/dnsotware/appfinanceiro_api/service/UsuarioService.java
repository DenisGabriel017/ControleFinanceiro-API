package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.config.tenant.TenantContext;
import com.dnsotware.appfinanceiro_api.dto.RegistroDTO;
import com.dnsotware.appfinanceiro_api.model.Usuario;
import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TenantService tenantService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService, TenantService tenantService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tenantService = tenantService;
    }

    public Usuario registrar(RegistroDTO data) {
        // Gera um tenantId único e seguro
        String tenantId = "tenant_" + UUID.randomUUID().toString().replace("-", "");

        // Cria o schema e define o contexto do tenant para a transação atual
        tenantService.createTenant(tenantId);
        TenantContext.setCurrentTenant(tenantId);

        if (usuarioRepository.findByEmail(data.email()) != null) {
            throw new RuntimeException("Usuário com esse email ja existe.");
        }
        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), senhaCriptografada);
        novoUsuario.setTenantId(tenantId);
        
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        
        // Limpa o contexto do tenant após a operação
        TenantContext.clear();

        return usuarioSalvo;
    }

    public String solicitarCodigoReset(String email) {
        // Para solicitar o código, precisamos encontrar o usuário em *qualquer* tenant.
        // Esta é uma operação mais complexa que pode exigir uma busca sem o filtro de tenant.
        // Por enquanto, vamos assumir que o contexto do tenant já está definido corretamente.
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
