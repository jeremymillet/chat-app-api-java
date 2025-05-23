package com.chatapp.api.service;

import com.chatapp.api.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import com.chatapp.api.dto.AuthRegisterRequest;
import com.chatapp.api.dto.AuthResponse;
import com.chatapp.api.dto.UserDTO;
import com.chatapp.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Authentifie un utilisateur, génère et retourne les tokens d'accès et de
     * rafraîchissement.
     */
    @Transactional
    public AuthResponse login(String email, String password) {
        Optional<User> userOptional = userService.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Vérifie si le mot de passe est correct
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Génère l'access token et le refresh token
                String accessToken = jwtUtil.generateAccessToken(Long.toString(user.getId()));
                String refreshToken = jwtUtil.generateRefreshToken(Long.toString(user.getId()));

                // Sauvegarde le refresh token en base (optionnel)
                user.setRefreshToken(refreshToken);
                userService.findUserByEmailAndUpdateRefreshToken(user.getId(), refreshToken);

                // Retourne les tokens et les informations utilisateur
                return new AuthResponse(accessToken, refreshToken, userService.getUserById(user.getId()));
            }
        }

        // Si l'authentification échoue, retourne null ou une exception
        throw new RuntimeException("Email ou mot de passe incorrect.");
    }

    /**
     * Enregistre un nouvel utilisateur avec un email, un nom d'utilisateur et un
     * mot de passe.
     */
    public UserDTO registerUser(AuthRegisterRequest authRequest) {
        // Vérifie si l'email est déjà utilisé
        if (userService.findUserByEmail(authRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        // Crée un nouvel utilisateur
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        // Sauvegarde l'utilisateur et retourne ses détails
        return userService.createUser(user);
    }

    /**
     * Rafraîchit l'access token en utilisant un refresh token valide.
     */
    public AuthResponse refreshAccessToken(String refreshToken) {
        // Valide le refresh token
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token invalide.");
        }

        // Récupère l'email depuis le refresh token
        String id = jwtUtil.getIdFromRefreshToken(refreshToken);

        // Récupère l'utilisateur
        Optional<User> userOptional = userService.findUserById(Long.parseLong(id));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new RuntimeException("Refresh token non reconnu.");
            }

            // Génère un nouveau access token
            String newAccessToken = jwtUtil.generateAccessToken(Long.toString(user.getId()));

            // Retourne le nouveau token
            return new AuthResponse(newAccessToken, refreshToken, userService.getUserById(user.getId()));
        } else {
            throw new RuntimeException("id non reconnu.");
        }
        // Vérifie si le refresh token correspond à celui en base
    }
    
    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
