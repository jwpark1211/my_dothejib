package com.soongkordan.dothejib;

import com.soongkordan.dothejib.domain.Family;
import com.soongkordan.dothejib.domain.FamilyMember;
import com.soongkordan.dothejib.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

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
    }
}
