package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.TodoDTO;
import com.soongkordan.dothejib.domain.*;
import com.soongkordan.dothejib.repository.CategoryRepository;
import com.soongkordan.dothejib.repository.FamilyMemberRepository;
import com.soongkordan.dothejib.repository.FamilyRepository;
import com.soongkordan.dothejib.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.TodoDTO.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired FamilyMemberRepository familyMemberRepository;
    @Autowired FamilyRepository familyRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TodoService todoService;
    @Autowired CategoryRepository categoryRepository;

    Member member1 = Member.builder().email("test1@email").password("password").build();
    Member member2 = Member.builder().email("test2@email").password("password").build();
    Member member3 = Member.builder().email("test3@email").password("password").build();
    Family family1 = Family.builder().name("family1").createdAt(LocalDateTime.now()).build();
    Family family2 = Family.builder().name("family2").createdAt(LocalDateTime.now()).build();
    FamilyMember familyMember1 = FamilyMember.builder().family(family1).member(member1).name("name1").build();
    FamilyMember familyMember2 = FamilyMember.builder().family(family1).member(member2).name("name1").build();
    FamilyMember familyMember3 = FamilyMember.builder().family(family1).member(member3).name("name1").build();
    FamilyMember familyMember4 = FamilyMember.builder().family(family2).member(member1).name("name1").build();
    Category category = Category.builder().name("category").build();

    @BeforeEach
    public void Before(){
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        familyRepository.save(family1);
        familyRepository.save(family2);
        categoryRepository.save(category);
        familyMemberRepository.save(familyMember1);
        familyMemberRepository.save(familyMember2);
        familyMemberRepository.save(familyMember3);
        familyMemberRepository.save(familyMember4);
    }

    @Test
    void saveTodoAndGetOneTodoInfo(){
        //given & When
        IdResponse todo1 = getTodo(
                "title1",LocalDate.now(),familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        //then
        assertEquals("title1",todoService.getTodoInfoWithId(todo1.getId()).getTitle());
    }

    @Test
    void getTodoWithFamilyId(){
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        IdResponse todo2 = getTodo(
                "title1",localDate,familyMember2.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo3 = getTodo(
                "title1",localDate,familyMember3.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo4 = getTodo(
                "title1",localDate,familyMember4.getId(),familyMember4.getId(),
                category.getId(),family2.getId());

        //when
        List<TodoInfoResponse> response = todoService.getTodoInfoWithFamilyId(family1.getId());

        //then
        assertEquals(3,response.size());
    }

    @Test
    void getTodoInfoWithPublisherId(){
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        IdResponse todo2 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo3 = getTodo(
                "title1",localDate,familyMember3.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo4 = getTodo(
                "title1",localDate,familyMember4.getId(),familyMember4.getId(),
                category.getId(),family2.getId());

        //when
        List<TodoInfoResponse> response = todoService.getTodoInfoWithPublisherId(familyMember1.getId());

        //then
        assertEquals(2,response.size());
    }

    @Test
    void getTodoInfoWithFamilyIdAndEndAt(){
        //given
        LocalDate localDate1 = LocalDate.of(2022,10,10);
        LocalDate localDate2 = LocalDate.of(2022,7,9);
        IdResponse todo1 = getTodo(
                "title1",localDate2,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        IdResponse todo2 = getTodo(
                "title1",localDate2,familyMember1.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo3 = getTodo(
                "title1",localDate1,familyMember3.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo4 = getTodo(
                "title1",localDate1,familyMember4.getId(),familyMember4.getId(),
                category.getId(),family2.getId());

        //when
        EndAtRequest endAtRequest = new EndAtRequest();
        endAtRequest.setEndAt(localDate1);
        List<TodoInfoResponse> response =
                todoService.getTodoInfoWithFamilyIdAndEndAt(family1.getId(),endAtRequest);

        //then
        assertEquals(1,response.size());
    }

    @Test
    void getTodoInfoWithPersonInChargeIdAndEndAt(){
        //given
        LocalDate localDate1 = LocalDate.of(2022,10,10);
        LocalDate localDate2 = LocalDate.of(2022,7,9);
        IdResponse todo1 = getTodo(
                "title1",localDate2,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        IdResponse todo2 = getTodo(
                "title1",localDate2,familyMember1.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo3 = getTodo(
                "title1",localDate2,familyMember3.getId(),familyMember2.getId(),
                category.getId(),family1.getId());
        IdResponse todo4 = getTodo(
                "title1",localDate1,familyMember4.getId(),familyMember4.getId(),
                category.getId(),family2.getId());

        //when
        EndAtRequest endAtRequest = new EndAtRequest();
        endAtRequest.setEndAt(localDate2);
        List<TodoInfoResponse> response =
                todoService.getTodoInfoWithPersonInChargeIdAndEndAt(familyMember2.getId(),endAtRequest);

        //then
        assertEquals(2,response.size());
    }

    @Test
    void modifyTodo(){
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());

        //when
        ModifyRequest request = new ModifyRequest();
        request.setTitle("modifyTitle");
        request.setCategoryId(category.getId());
        request.setPersonInChargeId(familyMember1.getId());

        todoService.modifyTodo(todo1.getId(),request);

        //then
        assertEquals("modifyTitle", todoService.getTodoInfoWithId(todo1.getId()).getTitle());
    }

    @Test
    void deleteTodo() throws Exception{
        assertThrows(IllegalArgumentException.class,()->{
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());

        //when
        todoService.deleteTodo(todo1.getId());

        //when
        todoService.getTodoInfoWithId(todo1.getId());
        });
    }

    @Test
    void completeTodo(){
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        LocalDateTime completedAt = LocalDateTime.of(2022,10,23,12,31);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());

        //when
        CompleteRequest request = new CompleteRequest();
        request.setCompletedAt(completedAt);
        todoService.completeTodo(todo1.getId(), request);

        //then
        assertEquals(completedAt,todoService.getTodoInfoWithId(todo1.getId()).getCompletedAt());

    }

    @Test
    void inCompleteTodo(){
        //given
        LocalDate localDate = LocalDate.of(2022,10,10);
        LocalDateTime completedAt = LocalDateTime.of(2022,10,23,12,31);
        IdResponse todo1 = getTodo(
                "title1",localDate,familyMember1.getId(),familyMember1.getId(),
                category.getId(),family1.getId());
        CompleteRequest request = new CompleteRequest();
        request.setCompletedAt(completedAt);
        todoService.completeTodo(todo1.getId(), request);

        //when
        todoService.inCompleteTodo(todo1.getId());

        //then
        assertEquals(null,todoService.getTodoInfoWithId(todo1.getId()).getCompletedAt());

    }

    private IdResponse getTodo(
            String title, LocalDate endAt, Long publisherId,
            Long personInCharge, Long categoryId, Long familyId
    ){
        SaveRequest request = new SaveRequest();
        request.setTitle(title);
        request.setEndAt(endAt);
        request.setPublisherId(publisherId);
        request.setPersonInChargeId(personInCharge);
        request.setCategoryId(categoryId);

        return todoService.saveTodo(request,familyId);
    }
   /* @Test
    void save_and_findOne() {
        // given
        Family family = getFamily("testFam");
        Member member = getMember("testEmail@test.com");
        Category category = getCategory(family, "Category1");
        FamilyMember familyMember = getFamilyMember(member, family, "fmName");
        Todo todo =
                Todo.builder()
                        .family(family)
                        .publisher(familyMember)
                        .title("title")
                        .category(category)
                        .difficulty(1)
                        .content("content")
                        .endAt(LocalDate.now())
                        .build();

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
                Todo.builder()
                        .family(family)
                        .personInCharge(familyMember)
                        .publisher(familyMember)
                        .title("title1")
                        .category(category)
                        .difficulty(1)
                        .content("content")
                        .endAt(LocalDate.of(2022,4,25))
                        .build();

        todoService.save(todo);
        return todo;
    }

    private Category getCategory(Family family, String name){
        Category category = Category.builder()
                .family(family)
                .name(name)
                .profileImg("")
                .description("desc")
                .build();
        categoryService.save(category);
        return category;
    }*/
}
