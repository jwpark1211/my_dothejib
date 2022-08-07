package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.controller.dto.FamilyMemberDTO;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FamilyMemberControllerTest {

    private MockMvc mvc;

    @MockBean private FamilyMemberService familyMemberService;
    @MockBean private FamilyService familyService;
    @MockBean private MemberService memberService;
    @Autowired private ObjectMapper objectMapper;

    Member member1 = Member.createMember("email@google.com", "password");
    Member member2 = Member.createMember("email2@google.com","password");
    Family family = Family.createFamily("name");

    List<FamilyMember> familyMembers = new ArrayList<>();
    Optional<FamilyMember> familyMember1
            = Optional.of(FamilyMember.createFamilyMember(member1,family,"name1","profileImg"));
    Optional<FamilyMember> familyMember2
            = Optional.of(FamilyMember.createFamilyMember(member2,family,"name2","profileImg"));
    Optional<FamilyMember> nullFamilyMember = Optional.empty();


    @BeforeEach
    void setUp(@Autowired FamilyMemberController familyMemberController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(familyMemberController).build();
        //MockMembers
        familyMembers.add(familyMember1.get());
        familyMembers.add(familyMember2.get());
    }

    public String toJsonString(FamilyMember familyMember)throws JsonProcessingException {
        return objectMapper.writeValueAsString(familyMember);
    }

    @Test
    @DisplayName("가족 구성원 생성")
    void save() throws Exception{
        //given
        when(memberService.findOne(any()))
                .thenReturn(Optional.of(member1));
        when(familyService.findOne(any()))
                .thenReturn(Optional.of(family));

        SaveRequest request = new SaveRequest();
        request.setMemberId(2L);
        request.setName("name");
        request.setProfileImg("image");

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/1/family-members/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }
    @Test
    @DisplayName("가족 구성원 생성 예외 by 멤버 아이디 오류")
    void saveException() throws Exception{
        //given
        when(memberService.findOne(any()))
                .thenReturn(Optional.empty());
        when(familyService.findOne(any()))
                .thenReturn(Optional.of(family));

        SaveRequest request = new SaveRequest();
        request.setMemberId(2L);
        request.setName("name");
        request.setProfileImg("image");

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/1/family-members/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("가족 구성원 단일 조회")
    void getOneFamilyMember() throws Exception{
        //given
        given(familyMemberService.findOne(any())).willReturn(familyMember1);

        //when
        ResultActions actions = mvc.perform(get("/families/1/family-members/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("name1"));
    }
    @Test
    @DisplayName("가족 구성원 단일 조회 예외")
    void getOneFamilyMemberException() throws Exception{
        //given
        given(familyMemberService.findOne(any())).willReturn(nullFamilyMember);

        //when
        ResultActions actions = mvc.perform(get("/families/1/family-members/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("가족 구성원 전체 조회")
    void getAllFamilyMember() throws Exception{
        //given
        given(familyMemberService.findByFamilyId(any())).willReturn(familyMembers);
        when(familyService.findOne(any()))
                .thenReturn(Optional.of(family));

        //when
        ResultActions actions = mvc.perform(get("/families/1/family-members"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].name").value("name1"));
    }

    @Test
    @DisplayName("가족 구성원 정보 수정")
    void modifyFamilyMember() throws Exception{
        //given
        given(familyMemberService.findOne(any())).willReturn(familyMember1);

        ModifyInfoRequest request = new ModifyInfoRequest();
        request.setName("modify");
        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(patch("/families/1/family-members/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
}
