package com.github.son_daehyeon.domain.auth.service;

import com.github.son_daehyeon.domain.auth.dto.request.LoginRequest;
import com.github.son_daehyeon.domain.auth.dto.request.RefreshTokenRequest;
import com.github.son_daehyeon.domain.auth.dto.request.RegisterRequest;
import com.github.son_daehyeon.domain.auth.dto.response.TokenResponse;
import com.github.son_daehyeon.domain.auth.exception.AuthExceptions;
import com.github.son_daehyeon.domain.auth.repository.RefreshTokenRedisRepository;
import com.github.son_daehyeon.domain.auth.schema.RefreshToken;
import com.github.son_daehyeon.domain.user.dto.response.UserResponse;
import com.github.son_daehyeon.domain.user.repository.UserRepository;
import com.github.son_daehyeon.domain.user.schema.User;
import com.github.son_daehyeon.global.security.authentication.UserAuthentication;
import com.github.son_daehyeon.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(AuthExceptions.AUTHENTICATION_FAILED::toException);

        UserAuthentication authentication = new UserAuthentication(user, dto.getPassword());
        authenticationManager.authenticate(authentication);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public Void register(RegisterRequest dto) {

        userRepository.findByEmail(dto.getEmail()).ifPresent((u) -> {
            throw AuthExceptions.ALREADY_REGISTERED.toException();
        });

        User user = User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .role(User.Role.MEMBER)
                .build();

        userRepository.save(user);

        return null;
    }

    public TokenResponse refreshToken(RefreshTokenRequest dto) {

        RefreshToken token = refreshTokenRedisRepository.findByToken(dto.getToken())
                .orElseThrow(AuthExceptions.INVALID_REFRESH_TOKEN::toException);

        refreshTokenRedisRepository.delete(token);

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(AuthExceptions.AUTHENTICATION_FAILED::toException);

        String accessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        return TokenResponse.of(accessToken, newRefreshToken);
    }

    public UserResponse me(User user) {

        return UserResponse.from(user);
    }
}
