package com.soongkordan.dothejib;

import com.soongkordan.dothejib.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Flow;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit(){
            System.out.println("<<<<<<<<<<<  INITIALIZE DATABASE  >>>>>>>>>>>>" );
            System.out.println();
            System.out.println("====  INITIALIZE MEMBER DATABASE  ====" );
            Member member1 = createMember("test1@gmail.com","password");
            Member member2 = createMember("test2@gmail.com","password");
            Member member3 = createMember("test3@gmail.com","password");
            Member member4 = createMember("test4@gmail.com","password");
            Member member5 = createMember("test5@gmail.com","password");
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);
            em.persist(member5);

            System.out.println("====  INITIALIZE FAMILY DATABASE  ====");
            Family family1 = createFamily("familyName1");
            Family family2 = createFamily("familyName2");
            em.persist(family1);
            em.persist(family2);

            System.out.println("====  INITIALIZE FAMILY MEMBER DATABASE  ====");
            FamilyMember familyMember1 = createFamilyMember(member1,family1,"fm name1","profileImg");
            FamilyMember familyMember2 = createFamilyMember(member2,family1,"fm name2","profileImg");
            FamilyMember familyMember3 = createFamilyMember(member3,family1,"fm name3","profileImg");
            FamilyMember familyMember4 = createFamilyMember(member3,family2,"fm name4","profileImg");
            FamilyMember familyMember5 = createFamilyMember(member4,family2,"fm name5","profileImg");
            FamilyMember familyMember6 = createFamilyMember(member5,family2,"fm name6","profileImg");
            em.persist(familyMember1);
            em.persist(familyMember2);
            em.persist(familyMember3);
            em.persist(familyMember4);
            em.persist(familyMember5);
            em.persist(familyMember6);

            System.out.println("====  INITIALIZE CATEGORY DATABASE  ====");
            Category category1 = createCategory(family1,"category1","img","desc");
            Category category2 = createCategory(family1,"category2","img","desc");
            Category category3 = createCategory(family2,"category3","img","desc");
            Category category4 = createCategory(family2,"category4","img","desc");
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);

            System.out.println("====  INITIALIZE TODO DATABASE  ====");
            LocalDate date1 = LocalDate.of(2022,6,8);
            LocalDate date2 = LocalDate.of(2021,3,7);
            Todo todo1 = createTodo("title1",family1,category1,familyMember1,familyMember2,date1,"content",2);
            Todo todo2 = createTodo("title2",family1,category1,familyMember3,familyMember2,date1,"content",3);
            Todo todo3 = createTodo("title3",family1,category2,familyMember1,familyMember3,date2,"content",2);
            Todo todo4 = createTodo("title4",family1,category2,familyMember2,familyMember2,date2,"content",7);
            Todo todo5 = createTodo("title5",family2,category3,familyMember4,familyMember4,date1,"content",8);
            Todo todo6 = createTodo("title6",family2,category3,familyMember5,familyMember4,date1,"content",8);
            Todo todo7 = createTodo("title7",family2,category4,familyMember4,familyMember4,date2,"content",9);
            Todo todo8 = createTodo("title8",family2,category4,familyMember6,familyMember5,date2,"content",5);
            em.persist(todo1);
            em.persist(todo2);
            em.persist(todo3);
            em.persist(todo4);
            em.persist(todo5);
            em.persist(todo6);
            em.persist(todo7);
            em.persist(todo8);

            System.out.println("<<<<<<<<<<<  END INITIALIZE DATABASE  >>>>>>>>>>>>" );
        }

        private Member createMember( String email, String password){
            return Member.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
        private Family createFamily(String name){
            return Family.builder()
                    .name(name)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        private FamilyMember createFamilyMember(
                Member member, Family family,String name, String profileImg){
            return FamilyMember.builder()
                    .member(member)
                    .family(family)
                    .name(name)
                    .profileImg(profileImg)
                    .build();
        }

        private Category createCategory(
                Family family, String name,
                String profileImg, String description
        ){
            return Category.builder()
                    .family(family)
                    .name(name)
                    .profileImg(profileImg)
                    .description(description)
                    .build();
        }

        private Todo createTodo(
                String title, Family family, Category category,
                FamilyMember publisher, FamilyMember personInCharge,
                LocalDate endAt, String content, int difficulty
        ){
            return Todo.builder()
                    .title(title)
                    .family(family)
                    .category(category)
                    .publisher(publisher)
                    .personInCharge(personInCharge)
                    .endAt(endAt)
                    .content(content)
                    .difficulty(difficulty)
                    .build();
        }
    }
}
