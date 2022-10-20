package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.TodoDTO;
import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Todo;
import com.soongkordan.dothejib.repository.CategoryRepository;
import com.soongkordan.dothejib.repository.FamilyMemberRepository;
import com.soongkordan.dothejib.repository.FamilyRepository;
import com.soongkordan.dothejib.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.TodoDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public IdResponse saveTodo(SaveRequest request, Long familyId){
        Todo todo;

        Family family = familyRepository.findById(familyId)
                .orElseThrow(()->new IllegalArgumentException("가족 정보가 없습니다."));
        FamilyMember publisher = familyMemberRepository.findById(request.getPublisherId())
                .orElseThrow(()->new IllegalArgumentException("발행자 정보가 없습니다."));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("카테고리 정보가 없습니다."));

        if(request.getPersonInChargeId()!=null) {
            FamilyMember personInCharge = familyMemberRepository.findById(request.getPersonInChargeId())
                    .orElseThrow(() -> new IllegalArgumentException("담당자 정보가 없습니다."));
            todo = Todo.builder()
                    .title(request.getTitle())
                    .difficulty(request.getDifficulty())
                    .content(request.getContent())
                    .endAt(request.getEndAt())
                    .family(family)
                    .category(category)
                    .publisher(publisher)
                    .personInCharge(personInCharge)
                    .build();
        }else {
            todo = Todo.builder()
                    .title(request.getTitle())
                    .difficulty(request.getDifficulty())
                    .content(request.getContent())
                    .endAt(request.getEndAt())
                    .family(family)
                    .category(category)
                    .publisher(publisher)
                    .build();
        }
        todoRepository.save(todo);
        return new IdResponse(todo.getId());
    }

    public TodoInfoResponse getTodoInfoWithId(Long todoId){
        return todoRepository.findById(todoId)
                .map(TodoInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("할 일 정보가 없습니다."));
    }

    public List<TodoInfoResponse> getTodoInfoWithFamilyId(Long familyId){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()->new IllegalArgumentException("가족 정보가 없습니다."));
        return todoRepository.findByFamilyId(familyId).stream()
                .map(TodoInfoResponse::of)
                .collect(Collectors.toList());
    }

    public List<TodoInfoResponse> getTodoInfoWithPublisherId(Long publisherId){
        FamilyMember publisher = familyMemberRepository.findById(publisherId)
                .orElseThrow(()-> new IllegalArgumentException("발행자 정보가 없습니다."));
        return todoRepository.findByPublisherId(publisherId).stream()
                .map(TodoInfoResponse::of)
                .collect(Collectors.toList());
    }

    public List<TodoInfoResponse> getTodoInfoWithFamilyIdAndEndAt(Long familyId, LocalDate endAt){
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()->new IllegalArgumentException("가족 정보가 없습니다."));
        return todoRepository.findByFamilyIdAndEndAt(familyId,endAt).stream()
                .map(TodoInfoResponse::of)
                .collect(Collectors.toList());
    }

    public List<TodoInfoResponse> getTodoInfoWithPersonInChargeIdAndEndAt(
            Long personInChargeId, LocalDate endAt
    ){
        FamilyMember personInCharge = familyMemberRepository.findById(personInChargeId)
                .orElseThrow(()-> new IllegalArgumentException("담당자 정보가 없습니다."));
        return todoRepository.findByPersonInChargeIdAndEndAt(personInChargeId,endAt).stream()
                .map(TodoInfoResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyTodo(Long todoId,ModifyRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new IllegalArgumentException("할 일 정보가 없습니다."));
        Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(()-> new IllegalArgumentException("카테고리 정보가 없습니다."));
        FamilyMember personInCharge = familyMemberRepository.findById(request.getPersonInChargeId())
                .orElseThrow(()-> new IllegalArgumentException("담당자 정보가 없습니다."));
        todo.update(personInCharge,request.getTitle(),category,request.getContent(),
                request.getDifficulty(),request.getEndAt());
    }

    @Transactional
    public void completeTodo(Long todoId, CompleteRequest request){
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new IllegalArgumentException("할 일 정보가 없습니다."));
        todo.completeTodo(request.getCompletedAt());
    }

    @Transactional
    public void inCompleteTodo(Long todoId){
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new IllegalArgumentException("할 일 정보가 없습니다."));
        todo.inCompleteTodo();
    }
}
