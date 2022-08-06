package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByFamilyId(Long familyId);
}
