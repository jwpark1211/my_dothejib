package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Category;
import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TodoDTO {

    @Getter @Setter
    public static class SaveRequest {
        @NotNull
        private Long publisherId;
        private Long personInChargeId;
        @NotEmpty
        private String title;
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endAt;

        @NotNull
        private Long categoryId;
        private int difficulty;
        private String content;
    }

    @Getter @Setter
    public static class CompleteRequest{
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime completedAt;
    }

    @Getter @Setter
    public static class getTodoRequest{
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endAt;
    }

    @Getter @Setter
    public static class ModifyRequest{
        private Long personInChargeId;
        private String title;
        private String content;
        private Long categoryId; //카테고리
        private int difficulty;
        private LocalDate endAt;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class TodoInfoResponse{
        private Long id; //투두 id
        private Long publisherId; //발급자
        private Long personInChargeId; //담당자
        private String title; //제목
        private String content; //내용
        private Long categoryId; //카테고리
        private int difficulty; //노동강도
        private LocalDateTime completedAt; //성취시간

        public static TodoInfoResponse of(Todo todo){
            return new TodoInfoResponse(
                    todo.getId(),todo.getPublisher().getId(),todo.getPersonInCharge().getId(), todo.getTitle(),
            todo.getContent(),todo.getCategory().getId(),todo.getDifficulty(),todo.getCompletedAt());
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class IdResponse{
        private Long id;
    }
}
