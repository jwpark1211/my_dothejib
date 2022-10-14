package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.domain.Authority;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원단일검색_ID(){
        //given
        Member member = getMember("test@gmail.com","password");
        //when
        Long savedId = memberService.saveMember(member);
        //then
        assertEquals(member.getEmail(),memberService.getMemberInfoWithId(savedId).getEmail());
    }
    @Test
    void 회원단일검색_EMAIL(){
        //given
        Member member = getMember("test@gmail.com","password");
        //when
        Long savedId = memberService.saveMember(member);
        //then
        assertEquals(member.getEmail(),memberService.getMemberInfoWithEmail("test@gmail.com").getEmail());
    }
    @Test
    void 모든회원검색(){
        //given
        List<MemberDTO.Response> initMembers = memberService.getAllMembers();

        Member member1 = getMember("test1@gmail.com","password");
        Member member2 = getMember("test2@gmail.com","password");
        Member member3 = getMember("test3@gmail.com","password");
        memberService.saveMember(member1);
        memberService.saveMember(member2);
        memberService.saveMember(member3);

        //when
        List<MemberDTO.Response> membersInfoList = memberService.getAllMembers();

        //then
        assertEquals(member1.getEmail(),membersInfoList.get(initMembers.size()).getEmail());
    }

    private Member getMember(String email, String password){
        return Member.builder()
                .email(email)
                .password(password)
                .authority(Authority.ROLE_USER)
                .build();
    }
}