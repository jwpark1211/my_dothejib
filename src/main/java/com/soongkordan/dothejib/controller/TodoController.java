package com.soongkordan.dothejib.controller;

import com.soongkordan.dothejib.controller.dto.CommonDTO;
import com.soongkordan.dothejib.controller.dto.FamilyDTO;
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

    //TODO: 코드가 너무 길어서 리팩토링 필요

    /*
    POST    Todo 생성
    GET     Todo 단일 조회
    GET     Todo 목록 조회(가족단위 )
    GET     Todo 목록 조회(가족구성원 단위 )
    PUT     Todo 수정
    DELETE  Todo 삭제
    POST    Todo Check( 완료 시간 생성 )
    DELETE  Todo UnCheck( 완료 시간 삭제 )
     */

    /*Todo 생성*/
    @PostMapping(path = "families/{family-id}/todos/new",produces =  APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> saveTodo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid SaveRequest request
    ){
        //family,publisher id 유효 여부 판단
        Optional<Family> family = familyService.findOne(request.getFamilyId());
        if(!family.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));

        Optional<FamilyMember> publisher = familyMemberService.findOne(request.getPublisherId());
        if(!publisher.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 발행자 정보가 없습니다. id를 확인해주세요."));

        //personInCharge id 입력 여부 판단 후 Todo 객체 생성
        Todo todo;
        if(request.getPersonInChargeId()!=null){
            Optional<FamilyMember> personInCharge = familyMemberService.findOne(request.getPersonInChargeId());
            if(!personInCharge.isPresent())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
            todo = Todo.createTodo(family.get(),publisher.get(),personInCharge.get(),
                    request.getTitle(),request.getDifficulty(),request.getContent(),request.getEndAt());
        }
        else{
            todo = Todo.createTodo(family.get(),publisher.get(),null,
                    request.getTitle(),request.getDifficulty(),request.getContent(),request.getEndAt());
        }

        Long savedId = todoService.save(todo);
        return ResponseEntity.ok() //return : 200 + todoId
                .body(new CommonResponse<CommonDTO.IdResponse>(
                        new CommonDTO.IdResponse(savedId)));
    }

    /*Todo 단일 조회*/
    @GetMapping(path = "families/{family-id}/todos/{todo-id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getOneTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ){
        //todoId 유효 여부 판단
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));

        Todo Todo = todo.get();
        return ResponseEntity.ok()
                .body(new CommonResponse<getTodoInfoResponse>(
                        new getTodoInfoResponse(
                                Todo.getId(),Todo.getPublisher().getId(),Todo.getPersonInCharge().getId(),Todo.getTitle(),
                                Todo.getContent(),Todo.getDifficulty(),Todo.getCompletedAt())));
    }

    /*Todo 목록 조회(가족단위)*/
    @GetMapping(path = "families/{family-id}/todos",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyTodo(
            @PathVariable("family-id") Long familyId,
            @RequestBody @Valid getTodoRequest request
    ){
        //familyId 유효 여부 판단
        Optional<Family> family = familyService.findOne(familyId);
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }

        //List<Entity> -> List<DTO>
        List<Todo> AllTodo = todoService.findByFamilyIdAndEndAt(familyId, request.getEndAt());
        List<getTodoInfoResponse> todoInfo = AllTodo.stream()
                .map(t -> new getTodoInfoResponse(
                        t.getId(), t.getPublisher().getId(),t.getPersonInCharge().getId(),t.getTitle()
                        ,t.getContent(), t.getDifficulty(),t.getCompletedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok() //return : 200 + todoInfoList
                .body(new CommonResponse<List>(todoInfo));
    }

    /*Todo 목록 조회(가족구성원 단위)*/
    @GetMapping(path = "families/{family-id}/family-members/{family-member-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMemberTodo(
            @PathVariable("family-member-id") Long personInChargeId,
            @RequestBody @Valid getTodoRequest request
    ){
        //PersonInChargeId 유효 여부 판단
        Optional<FamilyMember> personInCharge = familyMemberService.findOne(personInChargeId);
        if(!personInCharge.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
        }

        //List<Entity> -> List<DTO>
        List<Todo> AllTodo = todoService.findByPersonInChargeIdAndEndAt(personInChargeId,request.getEndAt());
        List<getTodoInfoResponse> todoInfo = AllTodo.stream()
                .map(t -> new getTodoInfoResponse(
                        t.getId(), t.getPublisher().getId(),t.getPersonInCharge().getId(),t.getTitle()
                        ,t.getContent(), t.getDifficulty(),t.getCompletedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok() //return : 200 + todoInfoList
                .body(new CommonResponse<List>(todoInfo));
    }

    /*Todo 수정*/
    @PutMapping(path = "families/{family-id}/todos/{todo-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId,
            @RequestBody @Valid modifyRequest request
    ) {
        //todoId 유효 여부 판단
        Optional<Todo> todo = todoService.findOne(todoId);
        if (!todo.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }

        //personInCharge Id 유효 여부 판단 및 수정 메소드 호출
        if (request.getPersonInChargeId() != null) {
            Optional<FamilyMember> personInCharge = familyMemberService.findOne(request.getPersonInChargeId());
            if (!personInCharge.isPresent())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));

            todoService.modifyTodo(todoId, personInCharge.get(), request.getTitle(),
                    request.getContent(), request.getDifficulty(), request.getEndAt());
        }
        else {
            todoService.modifyTodo(todoId, null, request.getTitle(),
                    request.getContent(), request.getDifficulty(), request.getEndAt());
        }

        return ResponseEntity.ok().build(); //return : 200
    }

    /*Todo 삭제*/
    @DeleteMapping(path = "families/{family-id}/todos/{todo-id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> deleteTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ){
        //todoId 유효 여부 판단
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }

        // 삭제
        todoService.deleteOne(todoId);
        return ResponseEntity.ok().build(); //return : 200
    }

    /*Todo Check( 완료 시간 생성 )*/
    @PostMapping(path = "families/{family-id}/todos/{todo-id}/complete",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> CompleteTodo(
            @RequestBody @Valid CompleteRequest request,
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ){
        //todoId가 유효한지 확인
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));

        //완료 시간(LocalDate) 생성
        todoService.completeTodo(todoId,request.getCompletedAt());
        return ResponseEntity.ok().build(); //return : 200
    }

    /*Todo UnCheck( 완료 시간 삭제 )*/
    @DeleteMapping(path = "families/{family-id}/todos/{todo-id}/complete",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> InCompleteTodo(
            @PathVariable("family-id") Long familyId,
            @PathVariable("todo-id") Long todoId
    ){
        //todoId가 유효한지 확인
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));

        //완료 시간(LocalDate) 삭제
        todoService.inCompleteTodo(todoId);
        return ResponseEntity.ok().build(); //return : 200
    }

}
