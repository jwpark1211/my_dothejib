package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.exception.BasicResponse;
import com.soongkordan.dothejib.controller.exception.CommonResponse;
import com.soongkordan.dothejib.controller.exception.ErrorResponse;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Todo;
import com.soongkordan.dothejib.service.FamilyMemberService;
import com.soongkordan.dothejib.service.FamilyService;
import com.soongkordan.dothejib.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static com.soongkordan.dothejib.controller.dto.TodoDTO.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final FamilyService familyService;
    private final FamilyMemberService familyMemberService;

    //TODO: 너무 길어서 코드 리팩토링 필요(검수 + 반환 한 번에 묶기).....
    /* Todo 생성*/
    @PostMapping(path = "/Todo/new",produces =  APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> save(
            @RequestBody @Valid SaveRequest request
    ){
        Optional<Family> family = familyService.findOne(request.getFamilyId());
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
        Optional<FamilyMember> publisher = familyMemberService.findOne(request.getPublisherId());
        if(!publisher.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 발행자 정보가 없습니다. id를 확인해주세요."));
        }
        Todo todo;
        if(request.getPersonInChargeId()!=null){
            System.out.println("null이 아닌 경우에");
            Optional<FamilyMember> personInCharge = familyMemberService.findOne(request.getPersonInChargeId());
            if(!personInCharge.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
            }
            todo = Todo.createTodo(family.get(),publisher.get(),personInCharge.get(),
                    request.getTitle(),request.getDifficulty(),request.getContent(),request.getEndAt());
        }
        else{
            todo = Todo.createTodo(family.get(),publisher.get(),null,
                    request.getTitle(),request.getDifficulty(),request.getContent(),request.getEndAt());
        }
        Long savedId = todoService.save(todo);
        return ResponseEntity.ok()
                .body(new CommonResponse<CommonDTO.IdResponse>(
                        new CommonDTO.IdResponse(savedId)));
    }

    /*Todo 완료 시간 생성 (Check)*/
    @PostMapping(path = "/Todo/complete/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> Complete(
            @RequestBody @Valid CompleteRequest request,
            @PathVariable("id") Long todoId
    ){
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }
        todoService.completeTodo(todoId,request.getCompletedAt());
        return ResponseEntity.status(HttpStatus.CREATED).build(); //return : 201
    }

    /*Todo 완료 시간 삭제(UnChecked)*/
    @DeleteMapping(path = "/Todo/Complete/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> InComplete(
            @PathVariable("id") Long todoId
    ){
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }
        todoService.inCompleteTodo(todoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
