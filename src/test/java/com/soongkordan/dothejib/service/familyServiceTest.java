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
    void modifyFamilyInfo(){

        //given
        Family family = Family.createFamily("name");
        familyService.save(family);

        //when
        Optional<Family> findFamily = familyService.findOne(family.getId());
        findFamily.get().modifyName("modify");

        //then
        assertEquals("modify",findFamily.get().getName());
    }
}
