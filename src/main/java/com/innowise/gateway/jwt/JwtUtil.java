package com.innowise.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        System.out.println("SECRET: " + secret);
        System.out.println("GATEWAY KEY: " + java.util.Base64.getEncoder().encodeToString(Keys.hmacShaKeyFor(secret.getBytes()).getEncoded()));
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
