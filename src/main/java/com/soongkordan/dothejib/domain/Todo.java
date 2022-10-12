package com.soongkordan.dothejib.domain;

import lombok.Builder;
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
    @JoinColumn(name = "personInCharge_id")
    private FamilyMember personInCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private FamilyMember publisher;

    private String title; //제목
    private int difficulty; //노동강도
    private String content; //내용
    private LocalDate endAt; //마감기한
    private LocalDateTime completedAt; //성취날짜

    @Builder
    public Todo( Family family, FamilyMember personInCharge, Category category,
                FamilyMember publisher, String title, int difficulty, String content, LocalDate endAt, LocalDateTime completedAt) {
        this.family = family;
        this.personInCharge = personInCharge;
        this.category = category;
        this.publisher = publisher;
        this.title = title;
        this.difficulty = difficulty;
        this.content = content;
        this.endAt = endAt;
        this.completedAt = completedAt;
    }

    public void completeTodo(LocalDateTime completedAt){
        if(this.completedAt!=null)
            throw new IllegalStateException("이미 완료된 할 일 입니다.");
        this.completedAt = completedAt;
    }

    public void inCompleteTodo(){
        if(this.completedAt==null)
            throw new IllegalStateException("아직 완료되지 않은 할 일 입니다.");
        this.completedAt = null;
    }

    public void modifyTodoInfo(
            FamilyMember personInCharge, String title, Category category, String content,int difficulty,LocalDate endAt
    ){
        this.personInCharge = personInCharge;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.content = content;
        this.endAt = endAt;
    }

    public void addPersonInCharge(FamilyMember personInCharge){
        this.personInCharge = personInCharge;
    }

    public void addCategory(Category category){
        this.category = category;
    }
}
