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

import static com.soongkordan.dothejib.controller.dto.CategoryDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final FamilyService familyService;

    /*카테고리 생성*/
    @PostMapping(path = "/family/{family-id}/category/new", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid SaveRequest request
    ) {
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(
                        categoryService.saveCategory(request,familyId)));
    }


    /*카테고리 단일 조회*/
    @GetMapping(path = "/family/{family-id}/category/{category-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getOneCategory(
            @PathVariable("family-id") Long familyId,
            @PathVariable("category-id") Long categoryId
    ) {
        return ResponseEntity.ok()
                .body(new CommonResponse<CategoryInfoResponse>(
                   categoryService.getCategoryInfoWithId(categoryId)));
    }

    /*카테고리 목록 조회(가족 id로)*/
    @GetMapping(path = "/family/{family_id}/categories", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllCategories(
            @PathVariable("family_id") Long familyId
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(
                        categoryService.getCategoryInfoWithFamilyId(familyId)));
    }
}
