package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class FamilyMember {

    @Id
    @Column(name = "familymember_id")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private String name;
    private String profileImg;

    public static FamilyMember createFamilyMember(
            Member member, Family family, String name, String profileImg
    ){
        FamilyMember familyMember = new FamilyMember();
        familyMember.member = member;
        familyMember.family = family;
        familyMember.name = name;
        familyMember.profileImg = profileImg;
        return familyMember;
    }
}
