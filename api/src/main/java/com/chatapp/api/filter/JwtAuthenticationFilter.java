package com.chatapp.api.filter;


import com.chatapp.api.service.CustomUserDetailsService;
import com.chatapp.api.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;




@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Exclure les endpoints /auth/** du filtrage JWT
        if (requestURI.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Log pour suivre le filtre
        System.out.println("JwtAuthenticationFilter: Filtrage en cours pour l'URL : " + request.getRequestURI());

        // 1. Récupérer le token depuis l'en-tête Authorization
        String token = getTokenFromRequest(request);
        System.out.println("Token extrait : " + token);

        // 2. Vérifier si le token est valide
        if (token != null) {
            System.out.println("test");
            try {
                System.out.println("test2");
            if (jwtUtil.validateAccessToken(token)) {
                String id = jwtUtil.getIdFromAccessToken(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        id, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            // Token expiré : laisse la requête passer pour renvoyer une erreur HTTP 401
            
            System.out.println("Token expiré : " + e.getMessage());
        }
}

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // Extraire le token de l'en-tête Authorization
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Récupère le token sans "Bearer "
                // 
        }
        return null;
    }
}