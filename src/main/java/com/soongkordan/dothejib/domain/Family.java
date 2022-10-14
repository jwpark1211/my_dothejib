package com.soongkordan.dothejib.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Family {

    @Id @Column(name="family_id")
    @GeneratedValue()
    private Long id;
    private String name;

    @OneToMany(mappedBy = "family")
    private List<Todo> todos = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Family (String name){
        this.name = name;
    }

    public void modifyName(String name){
        this.name = name;
    }
}
