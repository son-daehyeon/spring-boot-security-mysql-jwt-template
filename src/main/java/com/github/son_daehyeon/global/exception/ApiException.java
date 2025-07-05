package com.github.son_daehyeon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    public ApiException(ApiExceptionCode code) {

        super(code.getMessage());
    }

    public ApiException(String message) {

        super(message);
    }
}
