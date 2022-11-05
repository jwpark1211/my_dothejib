package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.soongkordan.dothejib.controller.dto.CategoryDTO.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(roles = "USER")
public class CategoryControllerTest {

    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    List<CategoryInfoResponse> categoryList = new ArrayList<>();

    CategoryInfoResponse response1 = new CategoryInfoResponse(1L, 2L, "name", "img", "desc");
    CategoryInfoResponse response2 = new CategoryInfoResponse(1L, 2L, "name", "img", "desc");
    CategoryInfoResponse response3 = new CategoryInfoResponse(1L, 2L, "name", "img", "desc");

    @BeforeEach
    void setUp(@Autowired CategoryController categoryController) {
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(ExceptionController.class)
                .build();
        //MockCategories
        categoryList.add(response1);
        categoryList.add(response2);
        categoryList.add(response3);
    }

    public String toJsonString(SaveRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    @Test
    @DisplayName("카테고리 생성")
    void save() throws Exception{
        //given
        SaveRequest request = new SaveRequest();
        request.setName("name");
        request.setProfileImg("img");
        request.setDescription("desc");

        String object = toJsonString(request);
        given(categoryService.saveCategory(any(),any())).willReturn(new IdResponse(1L));

        //when
        ResultActions actions =  mvc.perform(post("/family/1/category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("카테고리 생성 예외")
    void save_Exception() throws Exception{
        //given
        SaveRequest request = new SaveRequest();
        request.setName("name");
        request.setProfileImg("img");
        request.setDescription("desc");

        String object = toJsonString(request);
        given(categoryService.saveCategory(any(),any())).willThrow(IllegalArgumentException.class);

        //when
        ResultActions actions =  mvc.perform(post("/family/1/category/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("카테고리 단일 조회")
    void getOneCategory() throws Exception{
        //given
        given(categoryService.getCategoryInfoWithId(any())).willReturn(response1);

        //when
        ResultActions actions = mvc.perform(get("/family/1/category/2"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("name"));
    }

    @Test
    @DisplayName("카테고리 단일 조회 예외")
    void getOneCategory_Exception() throws Exception{
        //given
        given(categoryService.getCategoryInfoWithId(any())).willThrow(IllegalArgumentException.class);

        //when
        ResultActions actions = mvc.perform(get("/family/1/category/2"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("카테고리 목록 조회(가족 id로) ")
    void getAllCategories() throws Exception{
        //given
        given(categoryService.getCategoryInfoWithFamilyId(any())).willReturn(categoryList);

        //when
        ResultActions actions = mvc.perform(get("/family/1/categories"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    @DisplayName("카테고리 목록 조회(가족 id로) 예외")
    void getAllCategories_Exception() throws Exception{
        //given
        given(categoryService.getCategoryInfoWithFamilyId(any())).willThrow(IllegalArgumentException.class);

        //when
        ResultActions actions = mvc.perform(get("/family/1/categories"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("404"));
    }
}
