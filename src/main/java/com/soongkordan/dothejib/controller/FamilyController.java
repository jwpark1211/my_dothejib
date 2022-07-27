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

    /*가족 생성(저장)*/
    @PostMapping(path = "/family/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @RequestBody @Valid Request request
    ){
        Family family = Family.createFamily(request.getName());
        Long savedId = familyService.save(family);

       return ResponseEntity.ok()
               .body(new CommonResponse<CommonDTO.IdResponse>(
                       new CommonDTO.IdResponse(savedId))); //return : 200 + familyId
    }

    /*가족 정보(이름) 수정*/
    @PatchMapping(path = "/family/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyInfo(
            @PathVariable("id") Long id,
            @RequestBody @Valid Request request
    ){
        Optional<Family> family = familyService.modifyFamilyInfo(id,request.getName());
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build(); //return : 201
    }

    /*Id로 가족 정보 조회*/
    @GetMapping(path = "/family/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyInfo(
            @PathVariable("id") Long id
    ){
        Optional<Family> family = familyService.findOne(id);

        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
        return ResponseEntity.ok().body( //return: 200 + familyInfo(id/name/createdAt)
                new CommonResponse<Response>
                        (new Response(
                                family.get().getId(),family.get().getName(),family.get().getCreatedAt())));
    }
}

