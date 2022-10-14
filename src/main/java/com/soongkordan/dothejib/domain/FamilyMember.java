package com.soongkordan.dothejib.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FamilyMember {

    @Id
    @Column(name = "familymember_id")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private String name;
    private String profileImg;

    @Builder
    public FamilyMember (Member member, Family family, String name, String profileImg){
        this.member = member;
        this.family = family;
        this.name = name;
        this.profileImg = profileImg;
    }

    public void modifyName(String name){
        this.name = name;
    }

    public void modifyProfileImg(String profileImg){
        this.profileImg = profileImg;
    }
}
