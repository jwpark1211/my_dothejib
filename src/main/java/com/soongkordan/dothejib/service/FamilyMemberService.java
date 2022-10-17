package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.FamilyMemberRepository;
import com.soongkordan.dothejib.repository.FamilyRepository;
import com.soongkordan.dothejib.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;
    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public IdResponse saveFamilyMember(SaveRequest request,Long familyId){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()->new IllegalArgumentException("가족 정보가 없습니다."));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("유저 정보가 없습니다."));
        boolean duplicated = familyMemberRepository.existsByFamilyIdAndMemberId(familyId, member.getId());
        if(duplicated==false)
        {
            FamilyMember familyMember = FamilyMember.builder()
                    .member(member)
                    .family(family)
                    .name(request.getName())
                    .profileImg(request.getProfileImg())
                    .build();
        familyMemberRepository.save(familyMember);
        return new IdResponse(familyMember.getId());
        }else
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
    }

    public FamilyMemberInfoResponse getFamilyMemberInfoWithId(Long familyMemberId){
        return familyMemberRepository.findById(familyMemberId)
                .map(FamilyMemberInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("가족 구성원 정보가 없습니다."));
    }

    public List<FamilyMemberInfoResponse> getFamilyMembersInfoWithFamilyId(Long familyId){
        return familyMemberRepository.findByFamilyId(familyId).stream()
                .map(FamilyMemberInfoResponse::of)
                .collect(Collectors.toList());
    }

    public List<FamilyMemberInfoResponse> getFamilyMemberInfoWithMemberId(Long memberId){
        return familyMemberRepository.findByMemberId(memberId).stream()
                .map(FamilyMemberInfoResponse::of)
                .collect(Collectors.toList());
    }

    public FamilyMemberInfoResponse getFamilyMemberInfoWithFamilyIdAndMemberId(Long familyId, Long memberId){
        return familyMemberRepository.findByFamilyIdAndMemberId(familyId, memberId)
                .map(FamilyMemberInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("가족 구성원 정보가 없습니다."));
    }

    @Transactional
    public void modifyFamilyMemberInfo(Long familyMemberId, ModifyInfoRequest request){
        FamilyMember familyMember = familyMemberRepository.findById(familyMemberId)
                .orElseThrow(()->new IllegalArgumentException("가족 구성원 정보가 없습니다."));
        familyMember.update(request.getName(),request.getProfileImg());
    }
}
