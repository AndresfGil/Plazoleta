package com.pragma.powerup.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private static final String SECRET = "zW4nF8kP2rT7vX1yL9hG5bC3jM6qS0dE8oV1wA4iU7zW4nF8kP2rT7vX1yL9hG5b";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getCorreoFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getRolFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("rol", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getIdUsuarioFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("idUsuario", Long.class);
        } catch (Exception e) {
            return null;
        }
    }
}
