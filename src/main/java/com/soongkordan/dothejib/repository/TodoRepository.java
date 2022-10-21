package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.Todo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"family","category","personInCharge","publisher"})
    List<Todo> findByFamilyId(Long familyId);

    @EntityGraph(attributePaths = {"family","category","personInCharge","publisher"})
    List<Todo> findByPublisherId(Long publisherId);

    @EntityGraph(attributePaths = {"family","category","personInCharge","publisher"})
    List<Todo> findByFamilyIdAndEndAt(Long familyId, LocalDate endAt);

    @EntityGraph(attributePaths = {"family","category","personInCharge","publisher"})
    List<Todo> findByPersonInChargeIdAndEndAt(Long personInChargeId, LocalDate endAt);

}
