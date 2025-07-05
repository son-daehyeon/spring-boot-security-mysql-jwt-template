package com.github.son_daehyeon.domain.auth.controller;

import com.github.son_daehyeon.domain.auth.dto.request.LoginRequest;
import com.github.son_daehyeon.domain.auth.dto.request.RefreshTokenRequest;
import com.github.son_daehyeon.domain.auth.dto.request.RegisterRequest;
import com.github.son_daehyeon.domain.auth.dto.response.TokenResponse;
import com.github.son_daehyeon.domain.auth.service.AuthService;
import com.github.son_daehyeon.domain.user.dto.response.UserResponse;
import com.github.son_daehyeon.domain.user.schema.User;
import com.github.son_daehyeon.global.response.ApiResponse;
import com.github.son_daehyeon.global.security.guard.MemberPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "[Auth]")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {

        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest request) {

        return ApiResponse.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 갱신")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {

        return ApiResponse.ok(authService.refreshToken(request));
    }

    @MemberPermission
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회")
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal User user) {

        return ApiResponse.ok(authService.me(user));
    }
}
