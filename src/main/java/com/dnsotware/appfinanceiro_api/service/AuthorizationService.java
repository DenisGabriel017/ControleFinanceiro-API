package com.dnsotware.appfinanceiro_api.service;

import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService  implements UserDetailsService {

    private final UsuarioRepository repository;

    public AuthorizationService(UsuarioRepository repository){
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        java.util.Optional<UserDetails> usuarioOpt = java.util.Optional.ofNullable(repository.findByEmail(email));

        return usuarioOpt.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
