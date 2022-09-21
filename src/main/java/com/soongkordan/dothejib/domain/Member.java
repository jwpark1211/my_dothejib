package com.soongkordan.dothejib.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Getter
public class Member {

    @Id @Column(name = "member_id")
    @GeneratedValue
    private Long id;
    private String email;
    private String password;

    public static Member createMember(String email, String password){
        Member member = new Member();
        member.email = email;
        member.password = password;
        return member;
    }
}
