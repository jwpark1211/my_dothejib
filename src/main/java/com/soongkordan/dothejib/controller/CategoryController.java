package com.soongkordan.dothejib.controller;


import com.soongkordan.dothejib.controller.dto.CategoryDTO;
import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.service.CategoryService;
import com.soongkordan.dothejib.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final FamilyService familyService;

    @PostMapping(path = "/families/{family-id}/categories/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid CategoryDTO.SaveRequest request
    ) {
        // 중복된 코드이므로 따로 리팩토링 필요
        Optional<Family> family = familyService.findOne(familyId);
        if (!family.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));

        Category category = Category.createCategory(family.get(), request.getName(), request.getProfileImg(), request.getDescription());
        Long saveId = categoryService.save(category);
        return ResponseEntity.ok()
                .body(new CommonResponse<CommonDTO.IdResponse>(new CommonDTO.IdResponse(saveId)));
    }

    @GetMapping(path = "/families/{family-id}/categories/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getOneCategory(
            @PathVariable("family-id") Long familyId,
            @PathVariable("id") Long categoryId
    ) {
        Optional<Category> category = categoryService.findOne(categoryId);
        if(!category.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 카테고리 정보가 없습니다. id를 확인해주세요."));
        }
        Category categoryInst = category.get();
        return ResponseEntity.ok()
                .body(new CommonResponse<CategoryDTO.GetResponse>(
                        new CategoryDTO.GetResponse(categoryId, categoryInst.getFamily().getId(),
                                categoryInst.getName(), categoryInst.getProfileImg(), categoryInst.getDescription())
                ));
    }

    @GetMapping(path = "/families/{family_id}/categories", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllCategories(
            @PathVariable("family_id") Long familyId
    ){
        List<Category> categories = categoryService.findByFamilyId(familyId);

        List<CategoryDTO.GetResponse> categoryResponses = categories.stream()
                .map(ele -> new CategoryDTO.GetResponse(ele.getId(), ele.getFamily().getId(),
                        ele.getName(), ele.getProfileImg(), ele.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new CommonResponse<List>(categoryResponses));
    }
}
