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

    //TODO: familyMember 이미지 저장 구현

    /*
    POST    FamilyMember 생성
    GET     FamilyMember 단일 조회
    GET     FamilyMember 전체 조회 ( Family 소속 )
    PATCH   FamilyMember 정보 수정
     */

    /*FamilyMember 생성 */
    @PostMapping(path = "families/{family-id}/family-members/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveFamilyMember(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid SaveRequest request
    ){
        //member id, family id 유효 여부 판단
        Optional<Member> member = memberService.findOne(request.getMemberId());
        if(!member.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 회원 정보가 없습니다. id를 확인해주세요."));

        Optional<Family> family = familyService.findOne(familyId);
        if(!family.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));


        //FamilyMember 객체 생성
        FamilyMember familyMember =
                FamilyMember.builder()
                        .member(member.get())
                        .family(family.get())
                        .name(request.getName())
                        .profileImg(request.getProfileImg())
                        .build();
        familyMemberService.save(familyMember);

        return ResponseEntity.ok()
                .body(new CommonResponse<CommonDTO.IdResponse>(
                        new CommonDTO.IdResponse(familyMember.getId()))); //return: 200+ familyMemberId
    }

    /*FamilyMember 단일 조회 */
    @GetMapping(path = "/families/{family-id}/family-members/{family-member-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMember(
            @PathVariable("family-id") Long familyId,
            @PathVariable("family-member-id") Long familyMemberId
    ){
        //familyMember Id 유효 여부 판단
        Optional<FamilyMember> familyMember = familyMemberService.findOne(familyMemberId);
        // TODO: || familyMember.get().getFamily().getId() != familyId 예외처리 필요
        if(!familyMember.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 구성원 정보가 없습니다. id를 확인해주세요."));

        return ResponseEntity.ok() //return : 200 + Info(id,name,profileImg)
                .body(new CommonResponse<Response>(
                        new Response(familyMemberId,familyMember.get().getName(),familyMember.get().getProfileImg())));
    }

    /*FamilyMember 전체 조회 ( Family 소속 )*/
    @GetMapping(path = "/families/{family-id}/family-members",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllFamilyMembers(
            @PathVariable("family-id") Long familyId
    ){
        //familyId 유효 여부 판단
        Optional<Family> familyOP = familyService.findOne(familyId);
        if(!familyOP.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));

        //List<Entity> -> List<DTO>
        List<FamilyMember> familyMembers = familyMemberService.findByFamilyId(familyId);
        List<Response> familyMemberResponses = familyMembers.stream()
                .map(fm -> new Response(fm.getId(),fm.getName(),fm.getProfileImg()))
                .collect(Collectors.toList());

        return ResponseEntity.ok() //return : 200 + familyMemberList(Id/name/profileImg)
                .body(new CommonResponse<List>(familyMemberResponses));
    }

    /*FamilyMember 정보 수정*/
    //TODO: 이미지 수정까지 한 번에 처리
    @PatchMapping(path="/families/{family-id}/family-members/{family-member-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyMember(
            @PathVariable("family-id") Long familyId,
            @PathVariable("family-member-id") Long familyMemberId,
            @RequestBody ModifyInfoRequest request
    ){
        //familyMemberId 유효 여부 판단
        Optional<FamilyMember> familyMember =
                familyMemberService.findOne(familyMemberId);
        // TODO: || familyMember.get().getFamily().getId() != familyId 예외처리 필요
        if(!familyMember.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 구성원 정보가 없습니다. id를 확인해주세요."));

        //수정
        familyMemberService.modifyFamilyMemberInfo(familyMemberId,request.getName());
        return ResponseEntity.ok().build(); //return : 200
    }
}
