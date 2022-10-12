package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

@SpringBootTest
public class FamilyControllerTest {

    private MockMvc mvc;
    @MockBean private FamilyService familyService;
    @Autowired private ObjectMapper objectMapper;

    Optional<Family> family = Optional.of(Family.builder().name("name").build());
    Optional<Family> nullFamily = Optional.empty();

    @BeforeEach
    void setUp(@Autowired FamilyController familyController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(familyController).build();
    }

    public String toJsonString(Family family) throws JsonProcessingException {
        return objectMapper.writeValueAsString(family);
    }

    @Test
    @DisplayName("가족 생성")
    void save() throws Exception{

        //given
        String object = toJsonString(family.get());

        //when
        ResultActions actions = mvc.perform(post("/families/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("가족 정보 조회")
    void getFamilyInfo() throws Exception{

        //given
        given(familyService.findOne(any())).willReturn(family);

        //when
        ResultActions actions = mvc.perform(get("/families/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.name").value("name"));
    }

    @Test
    @DisplayName("가족 정보 조회 예외")
    void getFamilyInfoException() throws Exception{

        //given
        given(familyService.findOne(any())).willReturn(nullFamily);

        //when
        ResultActions actions = mvc.perform(get("/families/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
    }

}
