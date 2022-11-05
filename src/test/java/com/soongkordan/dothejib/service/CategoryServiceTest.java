package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.CategoryDTO;
import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.repository.FamilyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.soongkordan.dothejib.controller.dto.CategoryDTO.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WithMockUser(roles = "USER")
@Transactional
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    FamilyRepository familyRepository;

    Family family1 = Family.builder().name("family1").createdAt(LocalDateTime.now()).build();
    Family family2 = Family.builder().name("family2").createdAt(LocalDateTime.now()).build();

    @Test
    void saveAndGetOneTodo(){
        //given & when
        IdResponse category1 = getCategory(family1,"categoryName1");
        //then
        assertEquals("categoryName1",categoryService.getCategoryInfoWithId(category1.getId()).getName());
    }

    @Test
    void getCategoryInfoWithFamilyId(){
        //given
        IdResponse category1 = getCategory(family1,"categoryName1");
        IdResponse category2 = getCategory(family1,"categoryName1");
        IdResponse category3 = getCategory(family2,"categoryName1");

        //when
        List<CategoryInfoResponse> response = categoryService.getCategoryInfoWithFamilyId(family1.getId());

        //then
        assertEquals(2,response.size());
    }

    private IdResponse getCategory(
            Family family, String name
    ) {
        familyRepository.save(family);
        SaveRequest request = new SaveRequest();
        request.setName(name);
        request.setProfileImg("img");
        request.setDescription("desc");

        return categoryService.saveCategory(request,family.getId());
    }
}