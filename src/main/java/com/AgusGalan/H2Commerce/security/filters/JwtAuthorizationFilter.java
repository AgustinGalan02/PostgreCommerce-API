package com.AgusGalan.H2Commerce.security.filters;

import com.AgusGalan.H2Commerce.security.jwt.JwtUtils;
import com.AgusGalan.H2Commerce.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // se busca el token
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // Extrae el token de la cabecera "Authorization" si comienza con "Bearer "
            String token = tokenHeader.substring(7);

            if(jwtUtils.isTokenValid(token)) { // Verifica si el token es válido utilizando JwtUtils
                String username = jwtUtils.getUsernameFromToken(token);

                // Carga los detalles del usuario basados en el nombre de usuario desde el servicio UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crea un objeto UsernamePasswordAuthenticationToken con el nombre de usuario, sin contraseña (null), y los roles del usuario obtenidos
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                // Establece la autenticación en SpringSecurity
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    } // una vez por peticion. Se va a autenticar una vez por endpoint. Lo cual hace ncesario enviar el token siempre

}
