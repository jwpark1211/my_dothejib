package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.*;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Authority;
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

    /*
    POST    Member 회원가입
    POST    Member 로그인
    GET     Member 단일 조회
    GET     Member 전체 목록 조회
     */

    /*Member 회원가입*/
    //TODO: 회원가입 아이디, 비밀번호 포맷 결정 후 예외처리 필요
    @PostMapping(path = "/members/new",produces = APPLICATION_JSON_VALUE)
    public CommonDTO.IdResponse saveMember(
            @RequestBody @Valid Request request
    ){
        Member member = Member.builder()
                .authority(Authority.ROLE_USER)
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        Long savedId = memberService.join(member);

        return new CommonDTO.IdResponse(savedId);
    }

    /*Member 로그인*/
    //TODO: Security 추가
    @PostMapping(value = "/members/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> login(
            @RequestBody @Valid Request request
    ){
        //email 유효 여부 판단
        Optional<Member> member = memberService.findByEmail(request.getEmail());
        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse(
                            "일치하는 회원 정보가 없습니다. email 정보를 확인해주세요."));
        }

        //password 유효 여부 판단
        if(!member.get().getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //return : 401
                    .body(new ErrorResponse(
                            "회원 정보가 올바르지 않습니다. password 정보를 확인해주세요.","401"));
        }

        return ResponseEntity.ok().build(); //return : 200
    }

    /*Member 단일 조회*/
    @GetMapping(path = "/members/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findOneMember(
            @PathVariable("id") long id
    ){
        //memberId 유효 여부 판단
        Optional<Member> member = memberService.findOne(id);
        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
        }

        Member Member = member.get();
        return ResponseEntity.ok().body( //return : 200 + memberInfo(id/email)
                new CommonResponse<Response>
                        (new Response(Member.getId(),Member.getEmail())));
    }

    /*Member 전체 목록 조회*/
    @GetMapping(path = "/members", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findAllMembers(
    ){
        //List<Entity> -> List<DTO>
        List<Member> members = memberService.findMembers();
        List<Response> memberResponses = members.stream()
                .map(m -> new Response(m.getId(),m.getEmail()))
                .collect(Collectors.toList());

        return ResponseEntity.ok() //return : 200 + memberList(Id/email)
                .body(new CommonResponse<List>(memberResponses));
    }
}