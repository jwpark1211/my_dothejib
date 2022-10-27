package com.soongkordan.dothejib.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenDto{
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;
    }

    @Getter
    @NoArgsConstructor
    public static class TokenRequestDto{
        private String accessToken;
        private String refreshToken;
    }

}
