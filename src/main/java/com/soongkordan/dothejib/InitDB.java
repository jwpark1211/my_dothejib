package com.soongkordan.dothejib;

import com.soongkordan.dothejib.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
            System.out.println("====  INITIALIZE DATABASE  ====" );
            Member member1 = createMember("test1@gmail.com","password");
            Member member2 = createMember("test2@gmail.com","password");

            em.persist(member1);
            em.persist(member2);
        }

        private Member createMember( String email, String password){
            return Member.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }
}
