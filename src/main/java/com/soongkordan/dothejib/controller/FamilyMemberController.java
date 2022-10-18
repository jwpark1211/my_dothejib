package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    /*FamilyMember 생성*/
    @PostMapping(path = "family/{family-id}/family-member/new",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveFamilyMember(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid SaveRequest request
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(
                        familyMemberService.saveFamilyMember(request,familyId)));
    }

    /*FamilyMember 단일 조회 */
    @GetMapping(path = "/family/{family-id}/family-member/{family-member-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMember(
            @PathVariable("family-id") Long familyId,
            @PathVariable("family-member-id") Long familyMemberId
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<FamilyMemberInfoResponse>(
                        familyMemberService.getFamilyMemberInfoWithId(familyMemberId)));
    }

    /*FamilyMember 전체 조회 ( Family 소속 )*/
    @GetMapping(path = "/family/{family-id}/family-member/all",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllFamilyMembersWithFamilyId(
            @PathVariable("family-id") Long familyId
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(
                        familyMemberService.getFamilyMembersInfoWithFamilyId(familyId)));
    }

    /*FamilyMember 정보 수정*/
    //TODO: 이미지 수정까지 한 번에 처리
    @PatchMapping(path="/family/{family-id}/family-member/{family-member-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyMember(
            @PathVariable("family-id") Long familyId,
            @PathVariable("family-member-id") Long familyMemberId,
            @RequestBody ModifyInfoRequest request
    ){
        familyMemberService.modifyFamilyMemberInfo(familyMemberId,request);
        return ResponseEntity.ok().build();
    }
}
