package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.domain.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void 투두리스트_추가_And_단일검색() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo =
                Todo.createTodo(family,familyMember, null,
                        "title",1,"content", LocalDateTime.now());

        // when
        todoService.save(todo);

        // then
        assertEquals(todoService.findOne(todo.getId()).get(), todo);
    }

    @Test
    void 투두리스트_패밀리아이디_검색() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo1 = getTodo("testTodo1", family, familyMember);
        Todo todo2 = getTodo("testTodo2", family, familyMember);
        Todo todo3 = getTodo("testTodo3", family, familyMember);

        // when
        List<Todo> foundTodos = todoService.findByFamilyId(family.getId());

        // then
        assertEquals(foundTodos.size(), 3);
    }

    @Test
    void 투두리스트_투두발행자_검색() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo1 = getTodo("testTodo1", family, familyMember);
        Todo todo2 = getTodo("testTodo2", family, familyMember);
        Todo todo3 = getTodo("testTodo3", family, familyMember);

        // when
        List<Todo> foundTodos = todoService.findByPublisherId(familyMember.getId());

        // then
        assertEquals(foundTodos.size(), 3);
    }

    @Test
    void 투두리스트_추가_And_삭제() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo = getTodo("testTodo", family, familyMember);

        // when
        Long todoId = todoService.deleteOne(todo.getId());

        // then
        assertEquals(todo.getId(), todoId);
    }

    //=CreateMethod==//
    private Family getFamily(String name) {
        Family family = Family.createFamily(name);
        familyService.save(family);
        return family;
    }

    private Member getMember(String email) {
        Member member = Member.createMember(email, "password");
        memberService.join(member);
        return member;
    }

    private FamilyMember getFamilyMember(Member member, Family family, String nameFm) {
        FamilyMember familyMember =
                FamilyMember.createFamilyMember(member, family, nameFm, "profileImg");
        familyMemberService.save(familyMember);
        return familyMember;
    }

    private Todo getTodo(String title, Family family, FamilyMember familyMember) {
        Todo todo =
                Todo.createTodo(family,familyMember, null,
                        "title",1,"content", LocalDateTime.now());
        todoService.save(todo);
        return todo;
    }
}
