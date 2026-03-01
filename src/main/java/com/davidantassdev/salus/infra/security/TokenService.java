package com.davidantassdev.salus.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.davidantassdev.salus.domain.usuario.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            var token = JWT.create()
                    .withIssuer("salusApi")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
            log.info("Token generated successfully for user: {}", usuario.getLogin());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Error generating token for user: {}", usuario.getLogin(), exception);
            throw new RuntimeException("erro ao gerar Token", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            var subject = JWT.require(algoritmo)
                    .withIssuer("salusApi")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
            log.debug("Token verified successfully for user: {}", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            log.warn("Invalid or expired JWT token attempted");
            throw new RuntimeException("Token JWT invalido ou expirado!", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
