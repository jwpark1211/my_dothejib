package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.domain.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired
    FamilyMemberService familyMemberService;

    @Autowired
    FamilyService familyService;

    @Autowired
    MemberService memberService;

    @Autowired
    TodoService todoService;

    @Test
    void 투두리스트_추가_And_단일검색() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo = Todo.createTodo("testTodo", family, familyMember);

        // when
        todoService.save(todo);

        // then
        assertEquals(todoService.findOne(todo.getId()).get(), todo);
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
        Member member = Member.createMember(email,"password");
        memberService.join(member);
        return member;
    }
    private FamilyMember getFamilyMember(Member member, Family family, String nameFm) {
        FamilyMember familyMember =
                FamilyMember.createFamilyMember(member, family, nameFm,"profileImg");
        familyMemberService.save(familyMember);
        return familyMember;
    }

    private Todo getTodo(String title, Family family, FamilyMember familyMember){
        Todo todo = Todo.createTodo(title, family, familyMember);
        todoService.save(todo);
        return todo;
    }
}
