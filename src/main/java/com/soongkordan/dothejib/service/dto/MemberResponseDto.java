package com.soongkordan.dothejib.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
}
