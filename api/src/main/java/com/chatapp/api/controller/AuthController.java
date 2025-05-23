package com.chatapp.api.controller;

import com.chatapp.api.dto.AuthRegisterRequest;
import com.chatapp.api.dto.AuthRequest;
import com.chatapp.api.dto.AuthResponse;
import com.chatapp.api.dto.UserDTO;

import com.chatapp.api.service.AuthService;
import com.chatapp.api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        AuthResponse loginResponse = authService.login(authRequest.getEmail(), authRequest.getPassword());
        if (loginResponse != null) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)   // Rend le cookie accessible uniquement par le serveur
                .secure(true)     // Active HTTPS (désactiver en local si nécessaire)
                .sameSite("None") // Autorise le partage de cookie entre domaines différents
                .path("/")        // Rendre le cookie accessible à toutes les routes
                .maxAge(7 * 24 * 60 * 60) // Expiration en 7 jours
                .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = authService.getCookieValue(request, "refreshToken");
        System.out.println("refreshToken: " + refreshToken);
        AuthResponse authResponse = authService.refreshAccessToken(refreshToken);
        if (authResponse != null) {
            // Retourner la réponse avec le nouvel access token
            return ResponseEntity.ok()
            .body(authResponse);
        }
        return ResponseEntity.status(401).body("refresh token a expirée");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequest user) {
        try {
            UserDTO createdUser = authService.registerUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            if (userService.findUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with email " + user.getEmail() + " already exists.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not register user due to an unexpected error.");
        }
    }
}
