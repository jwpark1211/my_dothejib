package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import static com.soongkordan.dothejib.controller.dto.FamilyDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;

    /*family 생성*/
    @PostMapping(path = "/new",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveFamily(
            @RequestBody @Valid SaveRequest request
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>
                        (familyService.saveFamily(request)));

    }

    /*family 단일 조회*/
    @GetMapping(path = "/{family-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyInfo(
            @PathVariable ("family-id") Long familyId
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<FamilyInfoResponse>(
                        familyService.getFamilyInfoWithId(familyId)));
    }

    /*family 정보 수정*/
    @PatchMapping(path = "/{family-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyInfo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid ModifyRequest request
    ){
        familyService.modifyFamilyInfo(familyId,request);
        return ResponseEntity.ok().build();
    }
}

