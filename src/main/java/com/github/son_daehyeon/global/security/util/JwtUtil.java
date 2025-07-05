package com.github.son_daehyeon.global.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.son_daehyeon.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.son_daehyeon.domain.auth.schema.RefreshToken;
import com.github.son_daehyeon.domain.user.schema.User;
import com.github.son_daehyeon.global.property.JwtProperty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperty jwtProperty;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {

        algorithm = Algorithm.HMAC256(jwtProperty.getKey());
    }

    public String generateAccessToken(User user) {

        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtProperty.getAccessTokenExpirationHours(), ChronoUnit.HOURS))
                .withClaim("id", user.getId().toString())
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {

        String token = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtProperty.getRefreshTokenExpirationHours(), ChronoUnit.HOURS))
                .sign(algorithm);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(token)
                .ttl(jwtProperty.getRefreshTokenExpirationHours())
                .build();

        refreshTokenRedisRepository.save(refreshToken);

        return token;
    }

    public UUID extractToken(String token) {

        return UUID.fromString(JWT.require(algorithm).build().verify(token).getClaim("id").asString());
    }

    public boolean validateToken(String token) throws TokenExpiredException {

        if (Objects.isNull(token)) {

            return false;
        }

        JWT.require(algorithm).build().verify(token);

        return true;
    }
}
