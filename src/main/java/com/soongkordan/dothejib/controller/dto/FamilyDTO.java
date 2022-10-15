package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Family;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class FamilyDTO {
    @Getter @Setter
    public static class Request {
        @NotEmpty
        private String name;
    }

    @Getter @Setter
    public static class SaveRequest {
        @NotEmpty
        private String name;
    }

    @Getter @Setter
    public static class ModifyRequest{
        @NotEmpty
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FamilyInfoResponse{
        private Long id;
        private String name;
        private LocalDateTime createdAt;
        public static FamilyInfoResponse of(Family family){
            return new FamilyInfoResponse(family.getId(),family.getName(),family.getCreatedAt());
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
    }
}
