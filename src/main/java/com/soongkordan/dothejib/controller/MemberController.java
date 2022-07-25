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

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // TODO: REST Api Controller test code 작성 학습 및 적용 필요
    // TODO: 회원가입 아이디, 비밀번호 포맷 결정 후 예외처리 필요
    /*회원가입*/
    @PostMapping(path = "/members/new",produces = APPLICATION_JSON_VALUE)
    public CommonDTO.IdResponse save(
            @RequestBody @Valid MemberDTO.Request request
    ){
        Member member = Member.createMember(request.getEmail(),request.getPassword());
        Long savedId = memberService.join(member);

        return new CommonDTO.IdResponse(savedId);
    }

    //TODO: Security 추가
    /*로그인*/
    @PostMapping(value = "/members/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> login(
            @RequestBody @Valid MemberDTO.Request request
    ){
        Optional<Member> member = memberService.findByEmail(request.getEmail());

        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "일치하는 회원 정보가 없습니다. email 정보를 확인해주세요."));
        }
        if(!member.get().getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "회원 정보가 올바르지 않습니다. password 정보를 확인해주세요.","401"));
        }
        return ResponseEntity.noContent().build(); //204 반환
    }

    //TODO: member가 한 명도 없는 경우에 exception 처리를 해야 하는지?
    /*모든 멤버 조회*/
    @GetMapping(path = "/members", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findAllMembers(){
        List<Member> members = memberService.findMembers();
        List<MemberDTO.Response> memberResponses = members.stream()
                .map(m -> new MemberDTO.Response(m.getId(),m.getEmail()))
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
                new CommonResponse<MemberDTO.Response>
                        (new MemberDTO.Response(member.get().getId(),member.get().getEmail())));
    }
}