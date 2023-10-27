package com.yuzarsif.awscognitomysql.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto {

    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private String scope;
    private Integer expiresIn;
}
