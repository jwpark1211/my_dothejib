package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired FamilyMemberService familyMemberService;
    @Autowired FamilyService familyService;
    @Autowired MemberService memberService;
    @Autowired TodoService todoService;

    @Autowired CategoryService categoryService;

    @Test
    void save_and_findOne() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo =
                Todo.createTodo(family,familyMember, null,
                        "title", category,1,"content", LocalDate.now());

        // when
        todoService.save(todo);

        // then
        assertEquals(todoService.findOne(todo.getId()).get(), todo);
    }

    @Test
    void findByFamilyId() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo1 = getTodo("testTodo1", category, family, familyMember);
        Todo todo2 = getTodo("testTodo2", category, family, familyMember);
        Todo todo3 = getTodo("testTodo3", category, family, familyMember);

        // when
        List<Todo> foundTodos = todoService.findByFamilyId(family.getId());

        // then
        assertEquals(foundTodos.size(), 3);
    }

    @Test
    void findByPublisherId() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo1 = getTodo("testTodo1", category, family, familyMember);
        Todo todo2 = getTodo("testTodo2", category, family, familyMember);
        Todo todo3 = getTodo("testTodo3", category, family, familyMember);

        // when
        List<Todo> foundTodos = todoService.findByPublisherId(familyMember.getId());

        // then
        assertEquals(foundTodos.size(), 3);
    }

    @Test
    void deleteOne() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo = getTodo("testTodo", category, family, familyMember);

        // when
        Long todoId = todoService.deleteOne(todo.getId());

        // then
        assertEquals(todo.getId(), todoId);
    }

    @Test
    void completeTodo(){
        //given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo = getTodo("testTodo", category, family, familyMember);

        //when
        LocalDateTime time = LocalDateTime.of(2022,5,4,12,30);
        todoService.completeTodo(todo.getId(),time);

        //then
        assertEquals(todo.getCompletedAt(),LocalDateTime.of(2022,5,4,12,30));
    }

    @Test
    void inCompleteTodo(){
        //given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo = getTodo("testTodo", category, family, familyMember);
        LocalDateTime time = LocalDateTime.of(2022,5,4,12,30);
        todoService.completeTodo(todo.getId(),time);

        //when
        todoService.inCompleteTodo(todo.getId());

        //then
        assertEquals(todo.getCompletedAt(),null);
    }

    @Test
    void modifyTodo(){
        //given
        Family family = getFamily("testFam");
        Category category = getCategory(family, "Category1");
        Member member1 = getMember("testEmail@test.com");
        Member member2 = getMember("testEmail2@test.com");
        FamilyMember familyMember1 = getFamilyMember(member1, family, "fmName");
        FamilyMember familyMember2 = getFamilyMember(member2,family,"fmName");
        Todo todo = getTodo("testTodo", category, family, familyMember1);

        //when
        todoService.modifyTodo(todo.getId(),familyMember2,"modify",category, "modify",
                10,LocalDate.of(2021,12,13));

        //then
        assertEquals(familyMember2,todo.getPersonInCharge());
    }

    @Test
    void findByFamilyIdAndEndAt(){
        //given
        Family family = getFamily("testFam");
        Category category = getCategory(family, "Category1");
        Member member1 = getMember("testEmail@test.com");
        Member member2 = getMember("testEmail2@test.com");
        FamilyMember familyMember1 = getFamilyMember(member1, family, "fmName");
        FamilyMember familyMember2 = getFamilyMember(member2,family,"fmName");
        Todo todo1 = getTodo("testTodo", category, family, familyMember1);
        Todo todo2  = getTodo("testTodo2",category, family,familyMember2);

        //when
        List<Todo> todos = todoService.findByFamilyIdAndEndAt(family.getId(),LocalDate.of(2022,4,25));

        //then
        assertEquals(todos.size(),2);
    }

    @Test
    void findByPersonInChargeIdAndEndAt(){
        //given
        Family family = getFamily("testFam");
        Category category = getCategory(family, "Category1");
        Member member1 = getMember("testEmail@test.com");
        Member member2 = getMember("testEmail2@test.com");
        FamilyMember familyMember1 = getFamilyMember(member1, family, "fmName");
        FamilyMember familyMember2 = getFamilyMember(member2,family,"fmName");
        Todo todo1 = getTodo("testTodo", category, family, familyMember1);
        Todo todo2  = getTodo("testTodo2", category, family,familyMember2);
        Todo todo3 = getTodo("testTodo3", category, family,familyMember2);

        //when
        List<Todo> todos = todoService.findByPersonInChargeIdAndEndAt(
                familyMember2.getId(),LocalDate.of(2022,4,25));

        //then
        assertEquals(todos.size(),2);
    }


    //=CreateMethod==//
    private Family getFamily(String name) {
        Family family = Family.builder().name(name).build();
        familyService.save(family);
        return family;
    }

    private Member getMember(String email) {
        Member member = Member.builder()
                .authority(Authority.ROLE_USER)
                .email(email)
                .password("password")
                .build();
        memberService.join(member);
        return member;
    }

    private FamilyMember getFamilyMember(Member member, Family family, String nameFm) {
        FamilyMember familyMember =
                FamilyMember.builder().member(member).family(family).name(nameFm).profileImg("profileImg").build();
        familyMemberService.save(familyMember);
        return familyMember;
    }

    private Todo getTodo(String title, Category category, Family family, FamilyMember familyMember) {
        Todo todo =
                Todo.createTodo(family,familyMember, familyMember,
                        "title", category,1,"content", LocalDate.of(2022,4,25));
        todoService.save(todo);
        return todo;
    }

    private Category getCategory(Family family, String name){
        Category category = Category.createCategory(family, name, "", "desc");
        categoryService.save(category);
        return category;
    }
}
