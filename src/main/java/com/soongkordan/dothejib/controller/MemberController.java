package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.annotation.JsonAlias;
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
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /*Member 이이디로 단일 조회*/
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findMemberWithId(
            @PathVariable("id") long id
    ){
        return ResponseEntity.ok(
                new CommonResponse<Response>(
                        memberService.getMemberInfoWithId(id)));
    }

    /*SecurityContext에 있는 유저 정보 조회*/
    @GetMapping(path = "/me", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?extends BasicResponse> getMyMemberInfo(){
        return ResponseEntity.ok()
                .body(new CommonResponse<Response>(memberService.getMyInfo()));
    }

    /*Member 이메일로 단일 조회*/
    @GetMapping(path = "/email/{email}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findMemberWithEmail(
            @PathVariable("email") String email
    ){
        return ResponseEntity.ok(
                new CommonResponse<Response>(
                        memberService.getMemberInfoWithEmail(email)));
    }

    /*Member 전체 목록 조회*/
    @GetMapping(path = "/all"   , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> findAllMembers(
    ){
        return ResponseEntity.ok() //return : 200 + memberList(Id/email)
                .body(new CommonResponse<List>(memberService.getAllMembers()));
    }
}