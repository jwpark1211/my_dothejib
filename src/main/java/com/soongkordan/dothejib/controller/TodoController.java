package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.dto.FamilyDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Todo;
import com.soongkordan.dothejib.service.CategoryService;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.TodoDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final FamilyService familyService;
    private final FamilyMemberService familyMemberService;
    private final CategoryService categoryService;

    /*Todo 생성*/
    @PostMapping(path = "/family/{family-id}/todo/new",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveTodo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid SaveRequest request
    ){
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(
                        todoService.saveTodo(request,familyId)));
    }

    /*Todo 단일 조회*/
    @GetMapping(path = "/family/{family-id}/todo/{todo-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getOneTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ) {
        return ResponseEntity.ok()
                .body(new CommonResponse<TodoInfoResponse>(
                   todoService.getTodoInfoWithId(todoId)));
    }

    /*Todo 목록 조회(가족단위)*/
    @GetMapping(path = "/family/{family-id}/todos",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyTodo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid EndAtRequest request
    ) {
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(
                   todoService.getTodoInfoWithFamilyIdAndEndAt(familyId,request)));
    }

    /*Todo 목록 조회(가족구성원 단위)*/
    @GetMapping(path = "/family/{family-id}/family-member/{family-member-id}/todos", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMemberTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("family-member-id") Long personInChargeId,
            @RequestBody @Valid EndAtRequest request
    ) {
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(
                        todoService.getTodoInfoWithPersonInChargeIdAndEndAt(personInChargeId,request)));
    }

    /*Todo 수정*/
    @PutMapping(path = "/family/{family-id}/todo/{todo-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId,
            @RequestBody @Valid ModifyRequest request
    ) {
        todoService.modifyTodo(todoId,request);
        return ResponseEntity.ok().build();
    }

    /*Todo 삭제*/
    @DeleteMapping(path = "/family/{family-id}/todo/{todo-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> deleteTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok().build();
    }

    /*Todo Check( 완료 시간 생성 )*/
    @PostMapping(path = "/family/{family-id}/todo/{todo-id}/complete",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> CompleteTodo(
            @RequestBody @Valid CompleteRequest request,
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ) {
        todoService.completeTodo(todoId,request);
        return ResponseEntity.ok().build();
    }

    /*Todo UnCheck( 완료 시간 삭제 )*/
    @DeleteMapping(path = "/family/{family-id}/todo/{todo-id}/complete",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> InCompleteTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ){
        todoService.inCompleteTodo(todoId);
        return ResponseEntity.ok().build();
    }

}
