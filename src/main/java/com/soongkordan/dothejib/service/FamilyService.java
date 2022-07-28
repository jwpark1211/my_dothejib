package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    @Transactional
    public Long save(Family family){
        familyRepository.save(family);
        return family.getId();
    }

    public Optional<Family> findOne(Long familyId) {
      Optional<Family> family = familyRepository.findById(familyId);
      return family;
    }

    public Optional<Family> findByName(String name) {
        Optional<Family> family = familyRepository.findByName(name);
        return family;
    }

    @Transactional
    public void modifyFamilyInfo(Long familyId, String name){
        Optional<Family> family = familyRepository.findById(familyId);
        family.get().modifyName(name);
    }
}
