package com.github.son_daehyeon.global.exception;

public interface ApiExceptionCode {

    String getMessage();

    default ApiException toException() {

        return new ApiException(this);
    }
}
