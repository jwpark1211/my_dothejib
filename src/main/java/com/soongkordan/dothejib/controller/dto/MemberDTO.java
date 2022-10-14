package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Member;
import lombok.*;

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
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String email;
        public static Response of(Member member){
            return new Response(member.getId(),member.getEmail());
        }
    }
}
