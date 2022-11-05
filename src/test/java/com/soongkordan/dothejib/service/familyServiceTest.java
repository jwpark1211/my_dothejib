package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.domain.Family;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.FamilyDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "USER")
@Transactional
public class familyServiceTest {

    @Autowired FamilyService familyService;

    @Test
    void 가족생성_가족단일검색(){
        //given
        SaveRequest request = new SaveRequest();
        request.setName("familyName");
        //when
        IdResponse response = familyService.saveFamily(request);
        //then
        assertEquals(familyService.getFamilyInfoWithId(response.getId()).getName(), "familyName");
    }
    @Test
    void 가족정보수정(){
        //given
        SaveRequest saveRequest = new SaveRequest();
        saveRequest.setName("familyName");

        IdResponse response = familyService.saveFamily(saveRequest);

        ModifyRequest modifyRequest = new ModifyRequest();
        modifyRequest.setName("modifyName");

        //when
        familyService.modifyFamilyInfo(response.getId(),modifyRequest);

        //then
        assertEquals(familyService.getFamilyInfoWithId(response.getId()).getName(), "modifyName");
    }
}
