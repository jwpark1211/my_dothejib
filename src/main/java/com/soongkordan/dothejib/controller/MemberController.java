package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.MemberService;
import com.soongkordan.dothejib.service.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    ) {
        Member member = Member.createMember(email, password);
        memberService.join(member);
        // TODO: Retrun 형식 관련 구현 필요
        return true;
    }

    @GetMapping(path = "/members", produces = APPLICATION_JSON_VALUE)
    public Object list() {
        List<Member> members = memberService.findMembers();
        // TODO: 이런식으로 DTO 만들어서 리턴하는 패턴이 괜찮은지 확인 필요
        List<MemberResponseDto> memberResponses = memberService.findMembers()
                .stream()
                .map(member -> new MemberResponseDto(member.getId(), member.getEmail()))
                .collect(Collectors.toList());
        return memberResponses;
    }

    @GetMapping(path = "/members/{id}", produces = APPLICATION_JSON_VALUE)
    public Object searchById(
            @PathVariable(value = "id") Long id
    ) {
        Member member = memberService.findOne(id);
        MemberResponseDto memberResponse = new MemberResponseDto(member.getId(), member.getEmail());
        return memberResponse;
    }

}