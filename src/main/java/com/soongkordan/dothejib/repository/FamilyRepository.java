package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family,Long> {

    Optional<Family> findByName(String name);

}
