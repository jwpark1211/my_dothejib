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
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //return : 204
    }

    /*Todo 목록 조회(가족단위)*/
    @GetMapping(path = "/FamilyTodo/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getAllTodo(
            @PathVariable("id") Long familyId,
            @RequestBody @Valid getTodoRequest request
    ){
        Optional<Family> family = familyService.findOne(familyId);
        if(!family.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 가족 정보가 없습니다. id를 확인해주세요."));
        }
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
    @GetMapping(path = "/FamilyMemberTodo/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getFamilyMemberTodo(
            @PathVariable("id") Long personInChargeId,
            @RequestBody @Valid getTodoRequest request
    ){
        Optional<FamilyMember> personInCharge = familyMemberService.findOne(personInChargeId);
        if(!personInCharge.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //return : 404
                    .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
        }
        List<Todo> AllTodo = todoService.findByPersonInChargeIdAndEndAt(personInChargeId,request.getEndAt());
        List<getTodoInfoResponse> todoInfo = AllTodo.stream()
                .map(t -> new getTodoInfoResponse(
                        t.getId(), t.getPublisher().getId(),t.getPersonInCharge().getId(),t.getTitle()
                        ,t.getContent(), t.getDifficulty(),t.getCompletedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok() //return : 200 + todoInfoList
                .body(new CommonResponse<List>(todoInfo));
    }

    /*Todo 삭제*/
    @DeleteMapping(path = "/Todo/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> deleteTodo(
            @PathVariable("id") Long todoId
    ){
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }
        todoService.deleteOne(todoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //return : 204
    }

    /*Todo 수정*/
    @PutMapping(path = "/Todo/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> modifyTodo(
            @PathVariable("id") Long todoId,
            @RequestBody @Valid modifyRequest request
    ) {
        Optional<Todo> todo = todoService.findOne(todoId);
        if (!todo.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }
        if (request.getPersonInChargeId() != null) {
            Optional<FamilyMember> personInCharge = familyMemberService.findOne(request.getPersonInChargeId());
            if (!personInCharge.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("일치하는 담당자 정보가 없습니다. id를 확인해주세요."));
            }
            todoService.modifyTodo(todoId, personInCharge.get(), request.getTitle(),
                    request.getContent(), request.getDifficulty(), request.getEndAt());
        }
        else {
            todoService.modifyTodo(todoId, null, request.getTitle(),
                    request.getContent(), request.getDifficulty(), request.getEndAt());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build(); //return : 201
    }

    /*Todo 조회*/
    @GetMapping(path = "/Todo/{id}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> getTodo(
            @PathVariable("id") Long todoId,
            @RequestBody @Valid getTodoRequest request
    ){
        Optional<Todo> todo = todoService.findOne(todoId);
        if(!todo.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("일치하는 할 일 정보가 없습니다. id를 확인해주세요."));
        }

        Todo TodoG = todo.get();
        return ResponseEntity.ok()
                .body(new CommonResponse<getTodoInfoResponse>(
                       new getTodoInfoResponse(
                            TodoG.getId(),TodoG.getPublisher().getId(),TodoG.getPersonInCharge().getId(),TodoG.getTitle(),
                            TodoG.getContent(),TodoG.getDifficulty(),TodoG.getCompletedAt())));
    }
}
