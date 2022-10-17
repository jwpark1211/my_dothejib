package com.soongkordan.dothejib.controller.dto;

import com.soongkordan.dothejib.domain.FamilyMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class FamilyMemberDTO {

    @Getter
    @Setter
    public static class SaveRequest {
        @NotNull
        private Long memberId;
        private String name;
        private String profileImg;
    }

    @Getter
    @Setter
    public static class ModifyInfoRequest{
        private String name;
        private String profileImg;
    }

    @Getter
    @AllArgsConstructor
    public static class FamilyMemberInfoResponse{
        private Long id;
        private String name;
        private String profileImg;

        public static FamilyMemberInfoResponse of(FamilyMember familyMember){
            return new FamilyMemberInfoResponse(
                    familyMember.getId(), familyMember.getName(), familyMember.getProfileImg()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class IdResponse{
        private Long id;
    }
}
