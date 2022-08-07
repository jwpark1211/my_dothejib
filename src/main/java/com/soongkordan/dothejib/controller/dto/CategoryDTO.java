package com.soongkordan.dothejib.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class CategoryDTO {

    @Getter @Setter
    public static class SaveRequest {

        @NotNull
        private Long familyId;

        @NotNull
        private String name;

        private String profileImg;

        private String description;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class GetResponse {
        private Long id;
        private Long familyId;
        private String name;
        private String profileImg;
        private String description;
    }
}
