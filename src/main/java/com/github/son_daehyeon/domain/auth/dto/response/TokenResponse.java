package com.github.son_daehyeon.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class TokenResponse {

    String accessToken;
    String refreshToken;
}
