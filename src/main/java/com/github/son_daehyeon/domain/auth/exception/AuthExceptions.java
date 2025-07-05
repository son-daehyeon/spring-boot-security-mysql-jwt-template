package com.github.son_daehyeon.domain.auth.exception;

import com.github.son_daehyeon.global.exception.ApiExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptions implements ApiExceptionCode {

    AUTHENTICATION_FAILED("인증에 실패했습니다."),
    ACCESS_TOKEN_EXPIRED("엑세스 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),
    ALREADY_REGISTERED("이미 등록된 사용자입니다."),
    ;

    private final String message;
}
