package com.caffein.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtService {

    private final PublicKey publicKey;

    public JwtService() throws Exception {
        this.publicKey = loadPublicKey("keys/local-only/public_key.pem");
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !isTokenExpired(claims);
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private PublicKey loadPublicKey(String pemPath) throws Exception {
        String key = readKeyFromResource(pemPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private String readKeyFromResource(String pemPath) throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(pemPath)) {
            if (in == null) {
                throw new IllegalArgumentException("Could not find key file: " + pemPath);
            }
            return new String(in.readAllBytes());
        }
    }
}