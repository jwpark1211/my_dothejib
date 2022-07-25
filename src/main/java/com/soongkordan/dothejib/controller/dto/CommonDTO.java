package com.soongkordan.dothejib.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class CommonDTO {

    @Getter
    @AllArgsConstructor
    public static class IdResponse{
        private Long id;
    }

    @Getter
    @AllArgsConstructor
    public static class StringResponse{
        private String response;
    }

}
