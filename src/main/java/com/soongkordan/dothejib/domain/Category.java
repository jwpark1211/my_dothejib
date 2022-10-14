package com.soongkordan.dothejib.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private String name;
    private String profileImg;
    private String description;

    @Builder
    public Category( Family family, String name, String profileImg, String description) {
        this.family = family;
        this.name = name;
        this.profileImg = profileImg;
        this.description = description;
    }
}
