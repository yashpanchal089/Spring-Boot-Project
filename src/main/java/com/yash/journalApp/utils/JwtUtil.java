package com.yash.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    public Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims == null ? null : claims.getExpiration();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // If parsing fails (invalid signature, malformed, expired, etc.) return null so callers can decide
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        Date exp = extractExpiration(token);
        return exp == null || exp.before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // Token valid for 1 hour
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * 60))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) {
            return !isTokenExpired(token);
    }

}
