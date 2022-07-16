package com.soongkordan.dothejib.domain;

import lombok.Getter;

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

}
