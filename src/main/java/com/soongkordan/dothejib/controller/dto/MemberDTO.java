package com.soongkordan.dothejib.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MemberDTO {
    @Getter @Setter
    public static class Request {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long id;
        private String email;
    }
}
