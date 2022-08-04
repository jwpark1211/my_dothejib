package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
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
        private Long familyId;
        @NotNull
        private Long publisherId;
        private Long personInChargeId;
        @NotEmpty
        private String title;
        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endAt;

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
    public static class modifyRequest{
        private Long personInChargeId;
        private String title;
        private String content;
        private int difficulty;
        private LocalDate endAt;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class getTodoInfoResponse{
        private Long id; //투두 id
        private Long publisherId; //발급자
        private Long personInChargeId; //담당자
        private String title; //제목
        private String content; //내용
        private int difficulty; //노동강도
        private LocalDateTime completedAt; //성취시간
    }
}
