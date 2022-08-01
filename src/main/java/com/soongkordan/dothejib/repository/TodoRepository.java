package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByFamilyId(Long familyId);

    List<Todo> findByPublisherId(Long publisherId);
}
