package com.pragma.powerup.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (token != null && jwtUtils.validateToken(token)) {
                String correo = jwtUtils.getCorreoFromToken(token);
                String rol = jwtUtils.getRolFromToken(token);
                Long idUsuario = jwtUtils.getIdUsuarioFromToken(token);
                
                if (correo != null && rol != null && idUsuario != null) {
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rol));
                    UsernamePasswordAuthenticationToken auth = 
                        new UsernamePasswordAuthenticationToken(correo, null, authorities);
                    
                    auth.setDetails(idUsuario);
                    
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                }
            }
        } catch (Exception e) {
            log.error("Error en autenticación: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
