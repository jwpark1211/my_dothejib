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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    /*가족 생성(저장)*/
    //Todo: 가족이 생성됨과 동시에 가족 구성원(Host)객체도 같이 생성 및 저장
    @PostMapping(path = "/family/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @RequestBody @Valid FamilyDTO.Request request
    ){
        Family family = Family.createFamily(request.getName());
        Long savedId = familyService.save(family);

       return ResponseEntity.ok()
               .body(new CommonResponse<CommonDTO.IdResponse>(
                       new CommonDTO.IdResponse(savedId)));
    }

    /*가족 정보(이름) 수정*/
    @PatchMapping(path = "/family/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyFamilyInfo(
            @PathVariable("id") Long id,
            @RequestBody @Valid FamilyDTO.Request request
    ){
        Optional<Family> family = familyService.modifyFamilyInfo(id,request.getName());
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
        return ResponseEntity.noContent().build(); //204 반환
    }

    /*Id로 가족 정보 조회*/
    @GetMapping(path = "/family/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyInfo(
            @PathVariable("id") Long id
    ){
        Optional<Family> family = familyService.findOne(id);

        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
        return ResponseEntity.ok().body(
                new CommonResponse<FamilyDTO.Response>
                        (new FamilyDTO.Response(
                                family.get().getId(),family.get().getName(),family.get().getCreatedAt())));
    }
}

