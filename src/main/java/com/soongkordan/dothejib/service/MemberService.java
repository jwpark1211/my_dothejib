package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * join : Long
    * findOne : Optional<Member>
    * findByEmail : Optional<Member>
    * findMembers :  List<Member>
     */

    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
