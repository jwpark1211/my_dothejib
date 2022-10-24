package com.soongkordan.dothejib.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soongkordan.dothejib.domain.*;
import com.soongkordan.dothejib.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static com.soongkordan.dothejib.controller.dto.TodoDTO.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
public class TodoControllerTest {

    private MockMvc mvc;

    @MockBean private TodoService todoService;
    @MockBean private FamilyMemberService familyMemberService;
    @MockBean private FamilyService familyService;
    @MockBean private MemberService memberService;
    @MockBean private CategoryService categoryService;

    @Autowired private ObjectMapper objectMapper;

    List<TodoInfoResponse> todoList = new ArrayList<>();
    LocalDateTime completedAt =  LocalDateTime.of(2022,10,24,3,8);
    LocalDateTime completedAt2 = LocalDateTime.of(2021,3,21,8,20);
    TodoInfoResponse response1 = new TodoInfoResponse(1L,2L, 3L,
            "title1","content1", 4L,10,completedAt);
    TodoInfoResponse response2 = new TodoInfoResponse(1L,2L, 3L,
            "title2","content2", 4L,10,completedAt);
    TodoInfoResponse response3 = new TodoInfoResponse(1L,2L, 3L,
            "title3","content3", 4L,10,completedAt);
    TodoInfoResponse response4 = new TodoInfoResponse(1L,2L, 3L,
            "title4","content4", 4L,10,completedAt);
    TodoInfoResponse response5 = new TodoInfoResponse(1L,2L, 3L,
            "title5","content5", 4L,10,completedAt);


    @BeforeEach
    void setUp(@Autowired TodoController todoController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(todoController)
                .setControllerAdvice(ExceptionController.class)
                .build();
        //MockTodos
        todoList.add(response1);
        todoList.add(response2);
        todoList.add(response3);
        todoList.add(response4);
        todoList.add(response5);
    }

    public String toJsonString(Todo todo)throws JsonProcessingException {
        return objectMapper.writeValueAsString(todo);
    }
    public String toJsonString(SaveRequest request)throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
    public String toJsonString(EndAtRequest request)throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
    public String toJsonString(ModifyRequest request)throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
    public String toJsonString(CompleteRequest request)throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    @Test
    @DisplayName("투두 생성")
    void save() throws Exception{
        //given
        SaveRequest request = new SaveRequest();
        request.setCategoryId(1L);
        request.setPublisherId(2L);
        request.setPersonInChargeId(3L);
        request.setTitle("title");
        request.setEndAt(LocalDate.now());

        String object = toJsonString(request);
        given(todoService.saveTodo(any(),any())).willReturn(new IdResponse(1L));

        //when
        ResultActions actions = mvc.perform(post("/family/1/todo/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }
    @Test
    @DisplayName("투두 생성 - 예외 ")
    void save_exception() throws Exception{
        //given
        SaveRequest request = new SaveRequest();
        request.setCategoryId(1L);
        request.setPublisherId(2L);
        request.setPersonInChargeId(3L);
        request.setTitle("title");
        request.setEndAt(LocalDate.now());

        String object = toJsonString(request);
        given(todoService.saveTodo(any(), any()))
                .willThrow((IllegalArgumentException.class));

        //when
        ResultActions actions = mvc.perform(post("/family/1/todo/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("투두 단일 조회")
    void getOneTodo() throws Exception{
        //given
        given(todoService.getTodoInfoWithId(any())).willReturn(response1);

        //when
        ResultActions actions = mvc.perform(get("/family/1/todo/2"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("title1"));
    }

    @Test
    @DisplayName("투두 단일 조회 - 예외")
    void getOneTodo_Exception() throws Exception{
        //given
        given(todoService.getTodoInfoWithId(any())).willThrow(IllegalArgumentException.class);

        //when
        ResultActions actions = mvc.perform(get("/family/1/todo/2"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("투두 목록 조회 (가족 단위)")
    void getFamilyTodos() throws Exception{
        //given
        given(todoService.getTodoInfoWithFamilyIdAndEndAt(any(),any())).willReturn(todoList);
        EndAtRequest request = new EndAtRequest();
        request.setEndAt(LocalDate.now());

        //when
        ResultActions actions = mvc.perform(get("/family/1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    @DisplayName("투두 목록 조회(가족 단위) - 예외")
    void getFamilyTodos_Exception() throws Exception{
        //given
        given(todoService.getTodoInfoWithFamilyIdAndEndAt(any(),any()))
                .willThrow(IllegalArgumentException.class);
        EndAtRequest request = new EndAtRequest();
        request.setEndAt(LocalDate.now());

        //when
        ResultActions actions = mvc.perform(get("/family/1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("투두 목록 조회 (가족 구성원 단위)")
    void getFamilyMemberTodo() throws Exception{
        //given
        given(todoService.getTodoInfoWithPersonInChargeIdAndEndAt(any(),any())).willReturn(todoList);
        EndAtRequest request = new EndAtRequest();
        request.setEndAt(LocalDate.now());

        //when
        ResultActions actions = mvc.perform(get("/family/1/family-member/2/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    @DisplayName("투두 목록 조회(가족 구성원 단위) - 예외")
    void getFamilyMemberTodo_Exception() throws Exception{
        //given
        given(todoService.getTodoInfoWithPersonInChargeIdAndEndAt(any(),any()))
                .willThrow(IllegalArgumentException.class);
        EndAtRequest request = new EndAtRequest();
        request.setEndAt(LocalDate.now());

        //when
        ResultActions actions = mvc.perform(get("/family/1/family-member/2/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    @Test
    @DisplayName("Todo 수정")
    void modifyTodo() throws Exception{
        //given
        ModifyRequest request = new ModifyRequest();
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setTitle("modifyTitle");
        request.setEndAt(LocalDate.now());

        //when
        ResultActions actions = mvc.perform(put("/family/3/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 삭제")
    void deleteTodo() throws Exception{
        //when
        ResultActions actions = mvc.perform(delete("/family/3/todo/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo Check(완료 시간 생성)")
    void completeTodo() throws Exception{
        //given
        CompleteRequest request = new CompleteRequest();
        request.setCompletedAt(LocalDateTime.now());

        //when
        ResultActions actions = mvc.perform(post("/family/3/todo/1/complete").contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo Check(완료 시간 해제)")
    void inCompleteTodo() throws Exception{
        //when
        ResultActions actions = mvc.perform(delete("/family/3/todo/1/complete"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
}
