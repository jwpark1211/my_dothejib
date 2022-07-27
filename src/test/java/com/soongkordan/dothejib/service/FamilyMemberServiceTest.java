package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FamilyMemberServiceTest {

    @Autowired FamilyMemberService familyMemberService;
    @Autowired FamilyService familyService;
    @Autowired MemberService memberService;

    @Test
    void save(){
        //given
        Member member = getMember("email@email.com");
        Family family = getFamily("name");

        //when
        FamilyMember familyMember = getFamilyMember(member, family, "nameFm");
        FamilyMember findFamilyMember = familyMemberService.findOne(familyMember.getId()).get();

        //then
        assertEquals(familyMember,findFamilyMember);
    }

    @Test
    void findByFamilyId(){
        //given
        Member member1 = getMember("email1@email.com");
        Member member2 = getMember("email2@email.com");
        Family family1 = getFamily("name1");
        Family family2 = getFamily("name2");
        FamilyMember familyMember1 = getFamilyMember(member1, family1, "nameFm1");
        FamilyMember familyMember2 = getFamilyMember(member2, family1, "nameFm2");

        //when
        List<FamilyMember> familyMembers = familyMemberService.findByFamilyId(family1.getId());

        //then
        assertEquals(2,familyMembers.size());
    }

    @Test
    void findByFamilyIdAndMemberId(){
        //given
        Member member1 = getMember("email1@email.com");
        Member member2 = getMember("email2@email.com");
        Family family1 = getFamily("name1");
        Family family2 = getFamily("name2");
        FamilyMember familyMember1 = getFamilyMember(member1, family1, "nameFm1");
        FamilyMember familyMember2 = getFamilyMember(member2, family1, "nameFm2");

        //when
        FamilyMember find =
                familyMemberService.findByFamilyIdAndMemberId(family1.getId(),member2.getId()).get();

        //then
        assertEquals("nameFm2",find.getName());
    }

    //=CreateMethod==//
    private Family getFamily(String name) {
        Family family = Family.createFamily(name);
        familyService.save(family);
        return family;
    }
    private Member getMember(String email) {
        Member member = Member.createMember(email,"password");
        memberService.join(member);
        return member;
    }
    private FamilyMember getFamilyMember(Member member, Family family, String nameFm) {
        FamilyMember familyMember =
                FamilyMember.createFamilyMember(member, family, nameFm,"profileImg");
        familyMemberService.save(familyMember);
        return familyMember;
    }
}
