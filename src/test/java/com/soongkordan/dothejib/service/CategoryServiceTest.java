package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    FamilyService familyService;

    @Test
    public void save_and_findOne() {
        // given
        Family family = getFamily("testFam");
        Category category = Category.createCategory(family, "some category", "", "desc");

        // when
        categoryService.save(category);

        // then
        assertEquals(categoryService.findOne(category.getId()).get(), category);
    }

    @Test
    public void findByFamilyId() {
        // given
        Family family = getFamily("testFam");
        Category category1 = getCategory(family, "category1");
        Category category2 = getCategory(family, "category2");
        Category category3 = getCategory(family, "category3");
        Category category4 = getCategory(family, "category4");

        // when
        List<Category> categories = categoryService.findByFamilyId(family.getId());

        // then
        assertEquals(categories.size(), 4);
    }

    private Category getCategory(Family family, String name){
        Category category = Category.createCategory(family, name, "", "desc");
        categoryService.save(category);
        return category;
    }

    private Family getFamily(String name) {
        Family family = Family.createFamily(name);
        familyService.save(family);
        return family;
    }
}