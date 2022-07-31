package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Family {

    @Id @Column(name="family_id")
    @GeneratedValue()
    private Long id;
    private String name;

    @OneToMany(mappedBy = "family")
    private List<Todo> todos = new ArrayList<>();

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
