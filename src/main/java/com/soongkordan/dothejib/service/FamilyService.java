package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static com.soongkordan.dothejib.controller.dto.FamilyDTO.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    @Transactional
    public IdResponse saveFamily(SaveRequest request){
        Family family = Family.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();
        familyRepository.save(family);
        return new IdResponse(family.getId());
    }

    public FamilyInfoResponse getFamilyInfoWithId(Long familyId){
        return familyRepository.findById(familyId)
                .map(FamilyInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("가족 정보가 없습니다."));
    }

    @Transactional
    public void modifyFamilyInfo(Long familyId, ModifyRequest request){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new IllegalArgumentException("가족 정보가 없습니다."));
        family.update(request.getName());
    }

}
