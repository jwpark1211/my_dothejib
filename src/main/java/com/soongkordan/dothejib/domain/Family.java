package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Family {

    @Id @Column(name="family_id")
    @GeneratedValue()
    private Long id;

    private String name;

}
