package com.soongkordan.dothejib.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
//TODO: 루틴 구현
public class Todo {

    @Id
    @Column(name = "todo_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charged_familyMember_id")
    private FamilyMember personInCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_familyMember_id")
    private FamilyMember publisher;

    private String title; //제목
    private int difficulty; //노동강도
    private String content; //내용
    private LocalDate endAt; //마감기한
    private LocalDateTime completedAt; //성취날짜

    public static Todo createTodo(
            Family family, FamilyMember publisher, FamilyMember personInCharge ,
            String title, int difficulty, String content, LocalDate endAt
    ){
        Todo todo = new Todo();
        todo.family = family;
        todo.publisher = publisher;
        todo.personInCharge = personInCharge;
        todo.title = title;
        todo.difficulty = difficulty;
        todo.content = content;
        todo.endAt = endAt;

        return todo;
    }
}
