package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.service.FamilyService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.soongkordan.dothejib.controller.dto.FamilyDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class FamilyControllerTest {

    private MockMvc mvc;
    @MockBean private FamilyService familyService;
    @Autowired private ObjectMapper objectMapper;

    Family family = getFamily("family");
    IdResponse idResponse = new IdResponse(1L);
    FamilyInfoResponse familyInfoResponse = new FamilyInfoResponse(2L,"name", LocalDateTime.now());

    @BeforeEach
    void setUp(@Autowired FamilyController familyController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(familyController)
                .setControllerAdvice(ExceptionController.class)
                .build();
    }

    public String toJsonString(Family family) throws JsonProcessingException {
        return objectMapper.writeValueAsString(family);
    }

    @Test
    @DisplayName("가족 생성")
    void save() throws Exception{
        //given
        String object = toJsonString(family);
        given (familyService.saveFamily(any())).willReturn(idResponse);

        //when
        ResultActions actions = mvc.perform(post("/family/new")
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
        given(familyService.getFamilyInfoWithId(any())).willReturn(familyInfoResponse);

        //when
        ResultActions actions = mvc.perform(get("/family/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("name"));
    }

    @Test
    @DisplayName("가족 정보 조회 예외")
    void getFamilyInfo_Exception() throws Exception{
        //given
        given(familyService.getFamilyInfoWithId(any())).willThrow(new IllegalArgumentException("가족 정보가 없습니다."));

        //when
        ResultActions actions = mvc.perform(get("/family/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("가족 정보가 없습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("가족 정보 수정")
    void modifyFamilyInfo() throws Exception{
        String object = toJsonString(family);

        //when
        ResultActions actions = mvc.perform(patch("/family/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
   private Family getFamily(String name) {
       return Family.builder()
               .name(name)
               .createdAt(LocalDateTime.now())
               .build();
   }
}
