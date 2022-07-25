package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Family {

    @Id @Column(name="family_id")
    @GeneratedValue()
    private Long id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();

    public static Family createFamily(String name){
        Family family = new Family();
        family.name = name;
        return family;
    }

    public void modifyName(String name){
        this.name = name;
    }
}
