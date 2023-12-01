package com.AgusGalan.H2Commerce.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey; // necesario para generar token
    @Value("${jwt.time.expiration}")
    private String timeExpiration;


    // CREAR TOKEN
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //VALIDAR TOKEN
    public boolean isTokenValid(String token){
        try {
            Jwts.parserBuilder() // LEE EL TOKEN
                    .setSigningKey(getSignatureKey())  // VERIFICA LA FIRMA
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.error("Token invalido, error: ".concat(e.getMessage()));
            return false;
        }
    }

    //OBTENER USERNAME
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    //OBTENER UN SOLO CLAIM. ES UN OBJETO CON DATOS DEL USUARIO
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) { // RECIBE UN TOKEN JWT QUE SE APLICA A LOS CLAIMS DEL TOKEN
        Claims claims = extractAllClaims(token); // OBTIENE TODOS LOS CLAIMS DEL TOKEN
        return claimsTFunction.apply(claims); // APLICA LOS CLAIMS OBTENIDOS Y DEVUELVE EL RESULTADO
    }

    //OBTENER INFO DEL TOKEN (CLAIMS)
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // LEE EL TOKEN
                .setSigningKey(getSignatureKey())  // VERIFICA LA FIRMA
                .build()
                .parseClaimsJws(token)
                .getBody(); // devuelve todos los claims del token
    }


    //OBTENER FIRMA SECRET KEY
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
