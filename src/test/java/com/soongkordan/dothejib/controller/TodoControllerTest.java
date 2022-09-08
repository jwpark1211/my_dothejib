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

@SpringBootTest
public class TodoControllerTest {

    private MockMvc mvc;

    @MockBean private TodoService todoService;
    @MockBean private FamilyMemberService familyMemberService;
    @MockBean private FamilyService familyService;
    @MockBean private MemberService memberService;
    @MockBean private CategoryService categoryService;

    @Autowired private ObjectMapper objectMapper;

    Member member1 = Member.createMember("email1@gmail.com","password");
    Member member2 = Member.createMember("email2@gmail.com","password");
    Family family = Family.createFamily("familyName");
    FamilyMember familyMember1 = FamilyMember.createFamilyMember(member1,family,"name","profileImg");
    FamilyMember familyMember2 = FamilyMember.createFamilyMember(member2,family,"name","profileImg");
    Category category  = Category.createCategory(family,"name","profileImg","description");

    List<Todo> todoList = new ArrayList<>();

    Optional<Todo> todo1 = Optional.of(Todo.createTodo(family,familyMember1,familyMember1,"title1",category,10,
            "content",LocalDate.now()));
    Optional<Todo> todo2 = Optional.of(Todo.createTodo(family,familyMember1,familyMember1,"title2",category,10,
            "content",LocalDate.now()));
    Optional<Todo> todo3 = Optional.of(Todo.createTodo(family,familyMember1,familyMember1,"title3",category,10,
            "content",LocalDate.now()));

    @BeforeEach
    void setUp(@Autowired TodoController todoController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(todoController).build();
        //MockTodos
        todoList.add(todo1.get());
        todoList.add(todo2.get());
        todoList.add(todo3.get());
    }

    public String toJsonString(Todo todo)throws JsonProcessingException {
        return objectMapper.writeValueAsString(todo);
    }

    @Test
    @DisplayName("투두 생성")
    void save() throws Exception{

        //given
        when(familyMemberService.findOne(any())).thenReturn(Optional.of(familyMember1));
        when(familyService.findOne(any())).thenReturn(Optional.of(family));
        when(categoryService.findOne(any())).thenReturn(Optional.of(category));

        SaveRequest request = new SaveRequest();
        request.setTitle("title");
        request.setEndAt(LocalDate.now());
        request.setDifficulty(10);
        request.setContent("content");
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setPublisherId(3L);

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/4/todos/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @DisplayName("투두 생성 - 예외: 가족 정보 없음")
    void save_exception1() throws Exception{

        //given
        when(familyMemberService.findOne(any())).thenReturn(Optional.of(familyMember1));
        when(familyService.findOne(any())).thenReturn(Optional.empty());
        when(categoryService.findOne(any())).thenReturn(Optional.of(category));

        SaveRequest request = new SaveRequest();
        request.setTitle("title");
        request.setEndAt(LocalDate.now());
        request.setDifficulty(10);
        request.setContent("content");
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setPublisherId(3L);

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/4/todos/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("투두 생성 - 예외: 가족 구성원 정보 없음")
    void save_exception2() throws Exception{

        //given
        when(familyMemberService.findOne(any())).thenReturn(Optional.empty());
        when(familyService.findOne(any())).thenReturn(Optional.of(family));
        when(categoryService.findOne(any())).thenReturn(Optional.of(category));

        SaveRequest request = new SaveRequest();
        request.setTitle("title");
        request.setEndAt(LocalDate.now());
        request.setDifficulty(10);
        request.setContent("content");
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setPublisherId(3L);

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/4/todos/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 발행자 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("투두 생성 - 예외: 카테고리 정보 없음")
    void save_exception3() throws Exception{

        //given
        when(familyMemberService.findOne(any())).thenReturn(Optional.of(familyMember1));
        when(familyService.findOne(any())).thenReturn(Optional.of(family));
        when(categoryService.findOne(any())).thenReturn(Optional.empty());

        SaveRequest request = new SaveRequest();
        request.setTitle("title");
        request.setEndAt(LocalDate.now());
        request.setDifficulty(10);
        request.setContent("content");
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setPublisherId(3L);

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/4/todos/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 카테고리 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("투두 단일 조회")
    void getOneTodo() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);

        //when
        ResultActions actions = mvc.perform(get("/families/4/todos/6"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("title1"));

    }

    @Test
    @DisplayName("투두 단일 조회 예외")
    void getOneTodo_exception() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(Optional.empty());

        //when
        ResultActions actions = mvc.perform(get("/families/4/todos/6"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 목록 조회(가족단위)")
    void getFamilyTodos() throws Exception{
        //given
        given(familyService.findOne(any())).willReturn(Optional.of(family));
        given(todoService.findByFamilyIdAndEndAt(any(),any())).willReturn(todoList);

        getTodoRequest request = new getTodoRequest();
        request.setEndAt(LocalDate.now());
        String object = objectMapper.writeValueAsString(request);


        //when
        ResultActions actions = mvc.perform(get("/families/1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    @DisplayName("Todo 목록 조회(가족단위) 예외")
    void getFamilyTodos_exception() throws Exception{
        //given
        given(familyService.findOne(any())).willReturn(Optional.empty());
        given(todoService.findByFamilyIdAndEndAt(any(),any())).willReturn(todoList);

        getTodoRequest request = new getTodoRequest();
        request.setEndAt(LocalDate.now());
        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(get("/families/1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 목록 조회(가족구성원 단위)")
    void getFamilyMemberTodos() throws Exception{
        //given
        given(familyMemberService.findOne(any())).willReturn(Optional.of(familyMember1));
        given(todoService.findByPersonInChargeIdAndEndAt(any(),any())).willReturn(todoList);

        getTodoRequest request = new getTodoRequest();
        request.setEndAt(LocalDate.now());
        String object = objectMapper.writeValueAsString(request);


        //when
        ResultActions actions = mvc.perform(get("/families/1/family-members/2/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    @DisplayName("Todo 목록 조회(가족구성원 단위) 예외")
    void getFamilyMemeberTodos_exception() throws Exception{
        //given
        given(familyService.findOne(any())).willReturn(Optional.empty());
        given(todoService.findByPersonInChargeIdAndEndAt(any(),any())).willReturn(todoList);

        getTodoRequest request = new getTodoRequest();
        request.setEndAt(LocalDate.now());
        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(get("/families/1/family-members/2/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 수정")
    void modifyTodo() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);
        given(categoryService.findOne(any())).willReturn(Optional.of(category));
        given(familyMemberService.findOne(any())).willReturn(Optional.of(familyMember1));

        modifyRequest request = new modifyRequest();
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setContent("content");
        request.setTitle("title");
        request.setDifficulty(10);
        request.setEndAt(LocalDate.now());

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(put("/families/1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Todo 수정 예외 - todo id")
    void modifyTodo_exception1() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(Optional.empty());
        given(categoryService.findOne(any())).willReturn(Optional.of(category));
        given(familyMemberService.findOne(any())).willReturn(Optional.of(familyMember1));

        modifyRequest request = new modifyRequest();
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setContent("content");
        request.setTitle("title");
        request.setDifficulty(10);
        request.setEndAt(LocalDate.now());

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(put("/families/1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 수정 예외 - category id")
    void modifyTodo_exception2() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);
        given(categoryService.findOne(any())).willReturn(Optional.empty());
        given(familyMemberService.findOne(any())).willReturn(Optional.of(familyMember1));

        modifyRequest request = new modifyRequest();
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setContent("content");
        request.setTitle("title");
        request.setDifficulty(10);
        request.setEndAt(LocalDate.now());

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(put("/families/1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 카테고리 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 수정 예외 - personInCharge id")
    void modifyTodo_exception3() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);
        given(categoryService.findOne(any())).willReturn(Optional.of(category));
        given(familyMemberService.findOne(any())).willReturn(Optional.empty());

        modifyRequest request = new modifyRequest();
        request.setCategoryId(1L);
        request.setPersonInChargeId(2L);
        request.setContent("content");
        request.setTitle("title");
        request.setDifficulty(10);
        request.setEndAt(LocalDate.now());

        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(put("/families/1/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo 삭제")
    void deleteTodo() throws Exception{
        //given
        given(todoService.findOne(any())).willReturn(todo1);

        //when
        ResultActions actions = mvc.perform(delete("/families/1/todos/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo 삭제 예외")
    void deleteTodo_exception() throws Exception{
        //given
        given(todoService.findOne(any())).willReturn(Optional.empty());

        //when
        ResultActions actions = mvc.perform(delete("/families/1/todos/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo Check(완료 시간 생성)")
    void completeTodo() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);

        CompleteRequest request  = new CompleteRequest();
        request.setCompletedAt(LocalDateTime.now());
        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/2/todos/2/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo Check(완료 시간 생성) 예외")
    void completeTodo_exception() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(Optional.empty());

        CompleteRequest request  = new CompleteRequest();
        request.setCompletedAt(LocalDateTime.now());
        String object = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mvc.perform(post("/families/2/todos/2/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
    }

    @Test
    @DisplayName("Todo UnCheck(완료 시간 해제)")
    void inCompleteTodo() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(todo1);

        //when
        ResultActions actions = mvc.perform(delete("/families/2/todos/2/complete"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Todo UnCheck(완료 시간 해제) 예외")
    void inCompleteTodo_exception() throws Exception{

        //given
        given(todoService.findOne(any())).willReturn(Optional.empty());

        //when
        ResultActions actions = mvc.perform(delete("/families/2/todos/2/complete"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
    }
}
