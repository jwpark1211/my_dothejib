package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.dto.FamilyMemberDTO;
import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;
    private final MemberService memberService;
    private final FamilyService familyService;

    @PostMapping(path = "/familyMember/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @RequestBody @Valid SaveRequest request
    ){
        Optional<Member> member = memberService.findOne(request.getMemberId());
        Optional<Family> family = familyService.findOne(request.getFamilyId());

        if(!member.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 회원 정보가 없습니다. id를 확인해주세요."));
        }
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }

        FamilyMember familyMember =
                FamilyMember.createFamilyMember(member.get(),family.get(),request.getName(),request.getProfileImg());
        familyMemberService.save(familyMember);

        return ResponseEntity.ok()
                .body(new CommonResponse<CommonDTO.IdResponse>(
                        new CommonDTO.IdResponse(familyMember.getId())));
    }

    @GetMapping(path = "/familyMembers/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllFamilyMembersInfoByFamilyId(
            @PathVariable("id") Long familyId
    ){
        Optional<Family> familyOP = familyService.findOne(familyId);

        if(!familyOP.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }

        List<FamilyMember> familyMembers = familyMemberService.findByFamilyId(familyId);
        List<Response> familyMemberResponses = familyMembers.stream()
                .map(fm -> new Response(fm.getId(),fm.getName(),fm.getProfileImg()))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new CommonResponse<List>(familyMemberResponses));
    }

    @GetMapping(path = "/familyMember/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMemberInfo(
            @PathVariable("id") Long familyMemberId
    ){
        Optional<FamilyMember> familyMember = familyMemberService.findOne(familyMemberId);
        if(!familyMember.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 가족구성원 정보가 없습니다. id를 확인해주세요."));
        }

        return ResponseEntity.ok()
                .body(new CommonResponse<Response>(
                        new Response(familyMemberId,familyMember.get().getName(),familyMember.get().getProfileImg())));
    }
}
