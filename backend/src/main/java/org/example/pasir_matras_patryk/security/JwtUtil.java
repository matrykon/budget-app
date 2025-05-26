package org.example.pasir_matras_patryk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.pasir_matras_patryk.model.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Stabilny klucz, nie zmienia się po każdym uruchomieniu (ważne!)
    private final Key key = Keys.hmacShaKeyFor("mojaMegaBezpiecznaTajnaSekretnaFrazaDoJwtTokenow1234567890ABCDEF!@#".getBytes());

    // Generowanie tokena na podstawie użytkownika
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        long expirationMs = 3600000; // 1 godzina
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // dla kompatybilności
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // jeżeli parsowanie się uda, to OK
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

