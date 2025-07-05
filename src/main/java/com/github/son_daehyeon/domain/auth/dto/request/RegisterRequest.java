package com.github.son_daehyeon.domain.auth.dto.request;

import com.github.son_daehyeon.global.util.RegExp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
    String password;
}
