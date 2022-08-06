package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findOne(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category;
    }

    @Transactional
    public Long save(Category category) {
        categoryRepository.save(category);
        return category.getId();
    }

    public List<Category> findByFamilyId(Long familyId) {
        return categoryRepository.findByFamilyId(familyId);
    }
}
