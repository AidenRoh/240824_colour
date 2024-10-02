package com.colour.member.service;

import com.colour.member.dto.MemberRegisterDto;
import com.colour.member.dto.MemberSearchCond;
import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import com.colour.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member registerMember(MemberRegisterDto dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setPassword(dto.getPassword());
        member.setEmail(dto.getEmail());

        return memberRepository.create(member);
    }

    @Override
    public void updateMember(Long memberId, MemberUpdateDto dto) {
        memberRepository.update(memberId, dto);
    }

    @Override
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public List<Member> findAllMembersByCond(MemberSearchCond cond) {
        return List.of();
    }

    @Override
    public void deleteMember(Long memberId) {
        memberRepository.delete(memberId);
    }
}
