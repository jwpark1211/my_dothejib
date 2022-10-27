package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.Jwt.TokenProvider;
import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.controller.dto.MemberDTO.MemberRequest;
import com.soongkordan.dothejib.controller.dto.TokenDTO;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.domain.RefreshToken;
import com.soongkordan.dothejib.repository.MemberRepository;
import com.soongkordan.dothejib.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soongkordan.dothejib.controller.dto.MemberDTO.*;
import static com.soongkordan.dothejib.controller.dto.TokenDTO.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Response signUp(MemberRequest request){
        if(memberRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");

        Member member = request.toMember(passwordEncoder);
        return Response.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(MemberRequest request){
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();
        Authentication authentication = authenticationManagerBuilder
                .getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken()))
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(()->new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());

        refreshTokenRepository.save(newRefreshToken);
        return tokenDto;
    }
}
