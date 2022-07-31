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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charged_familymember_id")
    private FamilyMember personInCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_familymember_id")
    private FamilyMember publisher;

    private String content;

    public static Todo createTodo(String title, Family family, FamilyMember publisher){
        Todo todo = new Todo();
        todo.title = title;
        todo.family = family;
        todo.publisher = publisher;

        return todo;
    }
}
