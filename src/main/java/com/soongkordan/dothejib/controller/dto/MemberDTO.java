package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Authority;
import com.soongkordan.dothejib.domain.Member;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public static class MemberRequest{
        private String email;
        private String password;
        public Member toMember(PasswordEncoder passwordEncoder){
            return Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .authority(Authority.ROLE_USER)
                    .build();
        }
        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(email,password);
        }
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
