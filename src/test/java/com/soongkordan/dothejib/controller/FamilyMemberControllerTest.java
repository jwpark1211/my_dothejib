package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FamilyMemberControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(FamilyMemberControllerTest.class);
    private MockMvc mvc;
    @MockBean
    private FamilyMemberService familyMemberService;
    @MockBean
    private FamilyService familyService;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    List<Member> members = new ArrayList<>();
    Optional<Member> member = Optional.of(Member.createMember("test@email.com", "1234"));
    Optional<Family> family = Optional.of(Family.createFamily("testFamily"));

    Optional<FamilyMember> familyMember = Optional.of(FamilyMember.createFamilyMember(
            member.get(),
            family.get(),
            "testFamilyMemberName",
            "testImg"
    ));


    @BeforeEach
    void setUp(@Autowired FamilyMemberController familyMemberController) {
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(familyMemberController).build();

        //MockService
        given(memberService.findOne(any())).willReturn(member);
        given(familyService.findOne(any())).willReturn(family);
    }

    public String toJsonString(FamilyMember familyMember) throws JsonProcessingException {
        return objectMapper.writeValueAsString(familyMember);
    }

    @Test
    @DisplayName("가족 구성원 생성")
    void create() throws Exception {

        // given
        String object = toJsonString(familyMember.get());

        // when
        ResultActions actions = mvc.perform(post("/familyMember/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        // then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

    }
}
