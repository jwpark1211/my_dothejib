package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.MemberRepository;
import com.soongkordan.dothejib.service.MemberService;
import com.soongkordan.dothejib.service.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // TODO: Rest API Controller 구현이 적절한지 확인 필요
    // TODO: REST Api Controller test code 작성 학습 및 적용 필요
    // TODO: 회원가입 아이디, 비밀번호 포맷 결정 후 예외처리 필요
    @PostMapping(path = "/members/new", produces = APPLICATION_JSON_VALUE)
    public Object create(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password
    ) {
        Member member = Member.createMember(email, password);
        memberService.join(member);
        // TODO: Return 형식 관련 구현 필요
        return true;
    }

    // TODO: Return 형식 관련 구현 필요
    // TODO: 매우 단순한 로그인 기능. Security 관련 작업 하여 추후 릴리즈 시 반드시 개선 필요
    @PostMapping(value = "/members/login", produces = APPLICATION_JSON_VALUE)
    public Object login(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password
    ) {
        Optional<Member> member = memberRepository.findByEmail(email);
        System.out.println(member);
        System.out.println(email);
        System.out.println(password);
        if(!member.isPresent()) return false;
        if (member.get().getPassword().equals(password)){
            return true;
        } else {
            return false;
        }
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