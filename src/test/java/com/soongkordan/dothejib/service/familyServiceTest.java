package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class familyServiceTest {

    @Autowired FamilyService familyService;

    @Test
    void 가족생성and가족단일검색(){
        //given
        Family family = Family.builder().name("name").build();
        Long savedId = familyService.save(family);

        //when
        Family find = familyService.findOne(savedId).get();

        //then
        assertEquals(find,family);
    }

    @Test
    void 이름으로_가족_조회(){
        //given
        Family family = Family.builder().name("name").build();
        Long savedId = familyService.save(family);

        //when
        Family find = familyService.findByName("name").get();

        //then
        assertEquals(find,family);
    }

    @Test
    void 가족정보_수정(){
        //given
        Family family = Family.builder().name("name").build();
        Long savedId = familyService.save(family);

        //when
        familyService.modifyFamilyInfo(savedId,"modify");

        //then
        assertEquals("modify",family.getName());
    }
}
