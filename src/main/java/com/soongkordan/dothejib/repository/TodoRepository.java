package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByFamilyId(Long familyId);

    List<Todo> findByPublisherId(Long publisherId);

    List<Todo> findByFamilyIdAndEndAt(Long familyId, LocalDate endAt);

    List<Todo> findByPersonInChargeIdAndEndAt(Long personInChargeId, LocalDate endAt);
}
