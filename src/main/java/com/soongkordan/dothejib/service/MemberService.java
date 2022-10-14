package com.soongkordan.dothejib.service;

import com.soongkordan.dothejib.controller.dto.MemberDTO;
import com.soongkordan.dothejib.domain.Member;
import com.soongkordan.dothejib.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.soongkordan.dothejib.controller.dto.MemberDTO.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //email 로 유저 정보 찾아오기
    public Response getMemberInfoWithEmail(String email){
        return memberRepository.findByEmail(email)
                .map(Response::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    //member Id로 유저 정보 찾아오기
    public Response getMemberInfoWithId(Long memberId){
        return memberRepository.findById(memberId)
                .map(Response::of)
                .orElseThrow(()-> new RuntimeException("유저 정보가 없습니다."));
    }

    //모든 member 반환하기
    public List<Response> getAllMembers(){
        return  memberRepository.findAll().stream()
                .map(m -> new Response(m.getId(),m.getEmail()))
                .collect(Collectors.toList());
    }

    public Long saveMember(Member member){
        memberRepository.save(member);
        return member.getId();
    }

}
