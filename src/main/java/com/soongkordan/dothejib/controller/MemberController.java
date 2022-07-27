package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.*;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.MemberDTO.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //TODO: 로그인 / 회원가입 develop

    /*회원가입*/
    //TODO: 회원가입 아이디, 비밀번호 포맷 결정 후 예외처리 필요
    @PostMapping(path = "/members/new",produces = APPLICATION_JSON_VALUE)
    public CommonDTO.IdResponse save(
            @RequestBody @Valid Request request
    ){
        Member member = Member.createMember(request.getEmail(),request.getPassword());
        Long savedId = memberService.join(member);

        return new CommonDTO.IdResponse(savedId);
    }

    /*로그인*/
    //TODO: Security 추가
    @PostMapping(value = "/members/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> login(
            @RequestBody @Valid Request request
    ){
        Optional<Member> member = memberService.findByEmail(request.getEmail());

        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "일치하는 회원 정보가 없습니다. email 정보를 확인해주세요.")); //404
        }
        if(!member.get().getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "회원 정보가 올바르지 않습니다. password 정보를 확인해주세요.","401"));
        }
        return ResponseEntity.ok().build(); //200 반환
    }

    /*모든 멤버 조회*/
    @GetMapping(path = "/members", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findAllMembers(){
        List<Member> members = memberService.findMembers();
        List<Response> memberResponses = members.stream()
                .map(m -> new Response(m.getId(),m.getEmail()))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new CommonResponse<List>(memberResponses));
    }

    /*Id로 특정 멤버 조회*/
    @GetMapping(path = "/members/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> searchById(
            @PathVariable("id") long id
    ){
        Optional<Member> member = memberService.findOne(id);
        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }
        return ResponseEntity.ok().body(
                new CommonResponse<Response>
                        (new Response(member.get().getId(),member.get().getEmail())));
    }
}