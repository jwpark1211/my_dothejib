package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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

    public static Category createCategory(
        Family family, String name, String profileImg, String description
    ){
        Category category = new Category();
        category.family = family;
        category.name = name;
        category.profileImg = profileImg;
        category.description = description;

        return category;
    }

}
