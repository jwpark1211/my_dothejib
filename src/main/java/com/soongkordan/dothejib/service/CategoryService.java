package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.CategoryDTO;
import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.repository.CategoryRepository;
import com.soongkordan.dothejib.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.pool.TypePool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.CategoryDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public IdResponse saveCategory(SaveRequest request, Long familyId){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new IllegalArgumentException("가족 정보가 없습니다."));

        Category category = Category.builder()
                .family(family)
                .name(request.getName())
                .profileImg(request.getProfileImg())
                .description(request.getDescription())
                .build();

        categoryRepository.save(category);
        return new IdResponse(category.getId());
    }

    public CategoryInfoResponse getCategoryInfoWithId(Long categoryId){
        return categoryRepository.findById(categoryId)
                .map(CategoryInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("카테고리 정보가 없습니다."));
    }

    public List<CategoryInfoResponse> getCategoryInfoWithFamilyId(Long familyId){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new IllegalArgumentException("가족 정보가 없습니다."));
        return categoryRepository.findByFamilyId(familyId).stream()
                .map(CategoryInfoResponse::of)
                .collect(Collectors.toList());
    }
}
