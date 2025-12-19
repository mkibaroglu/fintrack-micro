package com.fintrack.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    private final long accessMinutes = 60;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes * 60);

        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(username)
                .setId(jti) // <-- jti
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public Instant getExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }


    public String getJti(String token) {
        return parseClaims(token).getId();
    }

    public boolean isSignatureValid(String token) {
        // parseClaims zaten signature invalid ise exception fırlatır
        parseClaims(token);
        return true;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
}

