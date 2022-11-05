package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.domain.Authority;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.soongkordan.dothejib.controller.dto.MemberDTO.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@WithMockUser(roles = "USER")
public class MemberControllerTest {

    private MockMvc mvc;
    @MockBean private MemberService memberService;
    @Autowired private ObjectMapper objectMapper;

    List<Response> members = new ArrayList<>();
    Member member1 = getMember("email1@gmail.com","password");
    Member member2 = getMember("email2@gmail.com","password");
    Member member3 = getMember("email3@gmail.com","password");
    Member member4 = getMember("email4@gmail.com","password");
    Member member5 = getMember("email5@gmail.com","password");

    @BeforeEach
    void setUp(@Autowired MemberController memberController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(ExceptionController.class)
                .build();
        //MockMembers
        members.add(Response.of(member1));
        members.add(Response.of(member2));
        members.add(Response.of(member3));
        members.add(Response.of(member4));
        members.add(Response.of(member5));
    }

    public String toJsonString(Member member) throws JsonProcessingException{
        return objectMapper.writeValueAsString(member);
    }


    /*멤버 아이디로 단일 조회 */
    @Test
    @DisplayName("멤버 아이디로 단일 조회")
    void findMemberWithId() throws Exception{
        //given
        given(memberService.getMemberInfoWithId(any())).willReturn(Response.of(member1));

        //when
        ResultActions actions = mvc.perform(get("/member/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("email1@gmail.com"));
    }

    /*멤버 아이디로 단일 조회 - 예외 */
    @Test
    @DisplayName("멤버 아이디로 단일 조회 - 예외")
    void findMemberWithId_Exception() throws Exception{
        //given
        given (memberService.getMemberInfoWithId(any())).willThrow(new IllegalArgumentException("유저 정보가 없습니다."));

        //when
        ResultActions actions = mvc.perform(get("/member/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("유저 정보가 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    /*멤버 이메일로 단일 조회 */
    @Test
    @DisplayName("멤버 이메일로 단일 조회")
    void findMemberWithEmail() throws Exception{
        //given
        given(memberService.getMemberInfoWithEmail(any())).willReturn(Response.of(member1));

        //when
        ResultActions actions = mvc.perform(get("/member/email/email1@gmail.com"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("email1@gmail.com"));
    }

    /*멤버 이메일로 단일 조회 - 예외 */
    @Test
    @DisplayName("멤버 이메일로 단일 조회 - 예외")
    void findMemberWithEmail_Exception() throws Exception{
        //given
        given (memberService.getMemberInfoWithEmail(any())).willThrow(new IllegalArgumentException("유저 정보가 없습니다."));

        //when
        ResultActions actions = mvc.perform(get("/member/email/noEmail@gmail.com"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("유저 정보가 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    /*멤버 전체 목록 조회 */
    @Test
    @DisplayName("멤버 전체 목록 조회")
    void findAllMembers() throws Exception{
        //given
        given(memberService.getAllMembers()).willReturn(members);

        //when
        ResultActions actions = mvc.perform(get("/member/all"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }

    private Member getMember(String email, String password){
        return Member.builder()
                .authority(Authority.ROLE_USER)
                .email(email)
                .password(password)
                .build();
    }
}
