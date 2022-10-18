package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.controller.dto.FamilyMemberDTO;
import com.soongkordan.dothejib.domain.Authority;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.MemberService;
import org.junit.jupiter.api.BeforeEach;

import static com.soongkordan.dothejib.controller.dto.FamilyMemberDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FamilyMemberControllerTest {

    private MockMvc mvc;

    @MockBean
    private FamilyMemberService familyMemberService;
    @MockBean
    private FamilyService familyService;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    List<FamilyMemberInfoResponse> responseList = new ArrayList<>();
    FamilyMemberInfoResponse response1 = new FamilyMemberInfoResponse(2L, "name1", "img");
    FamilyMemberInfoResponse response2 = new FamilyMemberInfoResponse(3L, "name2", "img");
    FamilyMemberInfoResponse response3 = new FamilyMemberInfoResponse(4L, "name3", "img");

    @BeforeEach
    void setUp(@Autowired FamilyMemberController familyMemberController) {
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(familyMemberController)
                .setControllerAdvice(ExceptionController.class)
                .build();
        responseList.add(response1);
        responseList.add(response2);
        responseList.add(response3);
    }

    public String toJsonString(FamilyMember familyMember) throws JsonProcessingException {
        return objectMapper.writeValueAsString(familyMember);
    }

    public String toJsonString(SaveRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    public String toJsonString(ModifyInfoRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    @Test
    @DisplayName("가족 구성원 생성")
    void save() throws Exception {
        //given
        SaveRequest request = new SaveRequest();
        request.setName("name");
        request.setMemberId(1L);
        request.setProfileImg("img");

        String object = toJsonString(request);
        given(familyMemberService.saveFamilyMember(any(), any()))
                .willReturn(new IdResponse(1L));

        //when
        ResultActions actions = mvc.perform(post("/family/2/family-member/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

    }

    @Test
    @DisplayName("가족 구성원 생성 예외 by 중복 회원")
    void save_Exception() throws Exception {
        //given
        SaveRequest request = new SaveRequest();
        request.setName("name");
        request.setMemberId(1L);
        request.setProfileImg("img");

        String object = toJsonString(request);
        given(familyMemberService.saveFamilyMember(any(), any()))
                .willThrow(new IllegalArgumentException("이미 존재하는 회원입니다."));

        //when
        ResultActions actions = mvc.perform(post("/family/2/family-member/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("이미 존재하는 회원입니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("가족 구성원 단일 조회")
    void getOneFamilyMember() throws Exception {
        //given
        given(familyMemberService.getFamilyMemberInfoWithId(any())).willReturn(response1);

        //when
        ResultActions actions = mvc.perform(get("/family/3/family-member/4"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("name1"));

    }

    @Test
    @DisplayName("가족 구성원 단일 조회 예외")
    void getOneFamilyMember_Exception() throws Exception {
        //given
        given(familyMemberService.getFamilyMemberInfoWithId(any()))
                .willThrow(new IllegalArgumentException("가족 구성원 정보가 없습니다."));

        //when
        ResultActions actions = mvc.perform(get("/family/3/family-member/4"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("가족 구성원 정보가 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));

    }

    @Test
    @DisplayName("가족 구성원 전체 조회")
    void getAllFamilyMember() throws Exception {
        //given
        given(familyMemberService.getFamilyMembersInfoWithFamilyId(any())).willReturn(responseList);

        //when
        ResultActions actions = mvc.perform(get("/family/1/family-member/all"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    @DisplayName("가족 구성원 전체 조회 예외")
    void getAllFamilyMemberException() throws Exception {
        //given
        given(familyMemberService.getFamilyMembersInfoWithFamilyId(any()))
                .willThrow(new IllegalArgumentException("가족 정보가 없습니다."));

        //when
        ResultActions actions = mvc.perform(get("/family/1/family-member/all"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("가족 정보가 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("가족 구성원 정보 수정")
    void modifyFamilyMemberInfo() throws Exception {
        //given
        ModifyInfoRequest request = new ModifyInfoRequest();
        request.setProfileImg("modifyImg");
        request.setName("modifyName");
        String object = toJsonString(request);

        //when
        ResultActions actions = mvc.perform(patch("/family/3/family-member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

}