package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class CategoryDTO {

    @Getter @Setter
    public static class SaveRequest {

        @NotNull
        private String name;

        private String profileImg;

        private String description;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class CategoryInfoResponse {
        private Long id;
        private Long familyId;
        private String name;
        private String profileImg;
        private String description;

        public static CategoryInfoResponse of(Category category){
            return new CategoryInfoResponse(category.getId(),category.getFamily().getId(),
                    category.getName(),category.getProfileImg(),category.getDescription());
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class IdResponse{
        private Long id;
    }
}
