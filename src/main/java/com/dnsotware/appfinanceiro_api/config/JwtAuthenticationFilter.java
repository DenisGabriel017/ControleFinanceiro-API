package com.dnsotware.appfinanceiro_api.config;

import com.dnsotware.appfinanceiro_api.repository.UsuarioRepository;
import com.dnsotware.appfinanceiro_api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    public JwtAuthenticationFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("--- FILTRO JWT INICIADO ---");
        System.out.println("URI: " + request.getRequestURI());

        if(request.getRequestURI().startsWith("/auth")){
            filterChain.doFilter(request,response);
            return;
        }

        var token = this.recuperarToken(request);

        if (token != null){
            var login = tokenService.getSubject(token);
            UserDetails usuario = repository.findByEmail(login); // Retorna o UserDetails

            if (usuario != null){
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println("--- FIM DO FILTRO ---");
        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request){
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}