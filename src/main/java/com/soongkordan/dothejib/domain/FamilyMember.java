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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private String name;

    private String profileImg;
}
