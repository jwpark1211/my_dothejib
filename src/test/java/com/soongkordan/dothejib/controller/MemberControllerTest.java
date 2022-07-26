package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MemberControllerTest {

    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Member> members = new ArrayList<>();
     Optional<Member> member = Optional.of(Member.createMember("email@google.com", "password"));
     Optional<Member> nullMember = Optional.empty();

    @BeforeEach
    void setUp(@Autowired MemberController memberController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(memberController).build();
        //MockMembers
        members.add(Member.createMember("email1@google.com","password"));
        members.add(Member.createMember("email2@google.com","password"));
        members.add(Member.createMember("email3@google.com","password"));
        members.add(Member.createMember("email4@google.com","password"));
        members.add(Member.createMember("email5@google.com","password"));
    }

    public String toJsonString(Member member) throws JsonProcessingException{
        return objectMapper.writeValueAsString(member);
    }

    @Test
    @DisplayName("멤버 전체 조회")
    void getAllMemberInfo() throws Exception{

        //given
        given(memberService.findMembers()).willReturn(members);

        //when
        final ResultActions actions = mvc.perform(get("/members"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].email").value("email1@google.com"));
    }

    @Test
    @DisplayName("id로 멤버 조회")
    void getMemberInfo() throws Exception{

        //given
        given(memberService.findOne(any())).willReturn(member);

        //when
        final ResultActions actions = mvc.perform(get("/members/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email")
                        .value("email@google.com"));
    }

    @Test
    @DisplayName("id로 멤버 조회 예외")
    void getMemberInfoException() throws Exception{

        //given
        given(memberService.findOne(any())).willReturn(nullMember);

        //when
        final ResultActions actions = mvc.perform(get("/members/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage")
                        .value("일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요."));
    }

}
