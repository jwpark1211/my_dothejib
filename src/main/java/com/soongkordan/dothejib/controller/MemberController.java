package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Objects;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // TODO: Rest API Controller 구현이 적절한지 확인 필요
    // TODO: REST Api Controller test code 작성 학습 및 적용 필요
    @PostMapping(path = "/members/new", produces = APPLICATION_JSON_VALUE)
    public Object create(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password
    ){
        Member member = Member.createMember(email, password);
        memberService.join(member);
        // TODO: Retrun 형식 관련 구현 필요
        return true;
    }

}
