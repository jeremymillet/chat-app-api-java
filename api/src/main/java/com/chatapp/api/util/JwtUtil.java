package com.chatapp.api.util;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key-access-token}")
    private String jwtSecretKeyAccessToken;

    @Value("${jwt.secret-key-refresh-token}")
    private String jwtSecretKeyRefreshToken;

    @Value("${jwt.expiration-time}")
    private Long jwtExpirationTime;

    private Key getSigningKeyAccessToken() {
        // Générer la clé HMAC en utilisant le secret en Base64
        byte[] keyBytes = Base64.getEncoder().encode(jwtSecretKeyAccessToken.getBytes());
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }
    
    private Key getSigningKeyRefreshToken() {
        // Générer la clé HMAC en utilisant le secret en Base64
        byte[] keyBytes = Base64.getEncoder().encode(jwtSecretKeyRefreshToken.getBytes());
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateAccessToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, id);
    }

    public String generateRefreshToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, id);
    }
    
    public String createRefreshToken(Map<String, Object> claims,String id) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime * 7)) // Durée plus longue
                .signWith(getSigningKeyRefreshToken())
                .compact();
    }

    private String createAccessToken(Map<String, Object> claims, String id) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(getSigningKeyAccessToken()) // Utilisation avec la clé générée
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKeyAccessToken())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKeyRefreshToken())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getIdFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKeyRefreshToken())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public String getIdFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKeyAccessToken())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}