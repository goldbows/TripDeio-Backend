package com.tripdeio.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${spring.security.key}")
    private String secret;

    private static final long EXPIRATION_DURATION = 1000 * 60 * 60 * 2; // 2 hours

    public String generateToken(String email, boolean isAdmin) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_DURATION);
        return JWT.create()
                .withSubject(email)
                .withClaim("isAdmin", isAdmin)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(secret));
    }

    public DecodedJWT decodeToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret)).build();
        return verifier.verify(token);
    }

    public String extractEmail(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean extractIsAdmin(String token) {
        return decodeToken(token).getClaim("isAdmin").asBoolean();
    }

    public boolean validateToken(String token, String email) {
        try {
            DecodedJWT jwt = decodeToken(token);
            return jwt.getSubject().equals(email) && jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
