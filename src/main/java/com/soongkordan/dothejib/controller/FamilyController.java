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

import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.FamilyDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    /*
    POST    Family 생성
    GET     Family 단일 조회
    PATCH   Family 정보 수정
     */

    /*Family 생성*/
    @PostMapping(path = "/families/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveFamily(
            @RequestBody @Valid Request request
    ){
        //family 객체 생성
        Family family = Family.createFamily(request.getName());
        Long savedId = familyService.save(family);

       return ResponseEntity.ok()
               .body(new CommonResponse<CommonDTO.IdResponse>(
                       new CommonDTO.IdResponse(savedId))); //return : 200 + familyId
    }

    /*Family 단일 조회*/
    @GetMapping(path = "/families/{family-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyInfo(
            @PathVariable("family-id") Long familyId
    ){
        //family id 유효 여부 판단
        Optional<Family> family = familyService.findOne(familyId);
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }

        Family Family = family.get();
        return ResponseEntity.ok().body( //return: 200 + familyInfo(id/name/createdAt)
                new CommonResponse<Response>
                        (new Response(
                                Family.getId(),Family.getName(),Family.getCreatedAt())));
    }

    /*Family 정보 수정*/
    @PatchMapping(path = "/families/{family-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyInfo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid Request request
    ){
        //family id 유효 여부 판단
        Optional<Family> family = familyService.findOne(familyId);
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }

        //수정
        familyService.modifyFamilyInfo(familyId,request.getName());
        return ResponseEntity.ok().build(); //return : 200
    }

}

