package com.soongkordan.dothejib.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class FamilyDTO {
    @Getter @Setter
    public static class Request {
        @NotEmpty
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long id;
        private String name;
        private LocalDateTime createdAt;
    }
}
