package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.controller.dto.FamilyMemberDTO;
import com.soongkordan.dothejib.domain.Authority;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.FamilyRepository;
import com.soongkordan.dothejib.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(roles = "USER")
@Transactional
public class FamilyMemberServiceTest {

    @Autowired FamilyMemberService familyMemberService;
    @Autowired FamilyRepository familyRepository;
    @Autowired MemberRepository memberRepository;

    Member member1 = Member.builder().email("test1@email").password("password").build();
    Member member2 = Member.builder().email("test2@email").password("password").build();
    Member member3 = Member.builder().email("test3@email").password("password").build();
    Family family1 = Family.builder().name("family1").createdAt(LocalDateTime.now()).build();
    Family family2 = Family.builder().name("family2").createdAt(LocalDateTime.now()).build();

    @Test
    void 가족구성원_저장(){
        //given & when
        IdResponse idResponse = getFamilyMember("name1",member1,family1);
        //then
        assertEquals("name1",familyMemberService.getFamilyMemberInfoWithId(idResponse.getId()).getName());
    }

    @Test
    void 가족아이디로_가족구성원_조회(){
        //given
        IdResponse idResponse1 = getFamilyMember("name1",member1,family1);
        IdResponse idResponse2 = getFamilyMember("name2",member2,family1);

        //when
        List<FamilyMemberInfoResponse> response = familyMemberService.getFamilyMembersInfoWithFamilyId(family1.getId());

        //then
        assertEquals(2,response.size());
    }

    @Test
    void 가족아이디와_유저아이디로_가족구성원_조회(){
        //given
        IdResponse idResponse1 = getFamilyMember("name1",member1,family1);
        IdResponse idResponse2 = getFamilyMember("name2",member2,family1);

        //when
        FamilyMemberInfoResponse response =
                familyMemberService.getFamilyMemberInfoWithFamilyIdAndMemberId(family1.getId(),member2.getId());

        //then
        assertEquals(response.getName(),"name2");
    }

    @Test
    void 유저아이디로_가족구성원_조회(){
        //given
        IdResponse idResponse1 = getFamilyMember("name1",member1,family1);
        IdResponse idResponse2 = getFamilyMember("name2",member1,family2);
        IdResponse idResponse3 = getFamilyMember("name3",member2,family2);

        //when
        List<FamilyMemberInfoResponse> response = familyMemberService.getFamilyMemberInfoWithMemberId(member1.getId());

        //then
        assertEquals(2,response.size());
    }

    @Test
    void 가족구성원_정보_수정(){
        //given
        IdResponse idResponse = getFamilyMember("name1",member1,family1);
        ModifyInfoRequest modifyRequest = new ModifyInfoRequest();
        modifyRequest.setName("modify");
        modifyRequest.setProfileImg("img");

        //when
        familyMemberService.modifyFamilyMemberInfo(idResponse.getId(),modifyRequest);

        //then
        FamilyMemberInfoResponse response = familyMemberService.getFamilyMemberInfoWithId(idResponse.getId());
        assertEquals(response.getName(),"modify");
    }
    private IdResponse getFamilyMember(
            String name, Member member, Family family
    ) {
        familyRepository.save(family);
        memberRepository.save(member);

        SaveRequest request = new SaveRequest();
        request.setName(name);
        request.setMemberId(member.getId());
        request.setProfileImg("img");

        return familyMemberService.saveFamilyMember(request,family.getId());
    }

}
