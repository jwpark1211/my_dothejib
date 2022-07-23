package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Todo {

    @Id
    @Column(name = "todo_id")
    @GeneratedValue
    private Long id;

    private String title;

    private LocalDateTime endAt;

    private int difficulty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charged_familymember_id")
    private FamilyMember personInCharge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_familymember_id")
    private FamilyMember publisher;

    private String content;
}
