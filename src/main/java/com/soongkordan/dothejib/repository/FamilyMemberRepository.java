package com.soongkordan.dothejib.repository;

import com.soongkordan.dothejib.domain.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember,Long> {

    List<FamilyMember> findByFamilyId(Long familyId);
    List<FamilyMember> findByMemberId(Long memberId);
    boolean existsByFamilyIdAndMemberId(Long familyId, Long memberId);
    Optional<FamilyMember> findByFamilyIdAndMemberId(Long familyId, Long memberId);

}
