package com.colour.member.service;

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

    private final MemberRepository repository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.repository = memberRepository;
    }

    @Override
    public Member registerMember(Member member) {
        return repository.create(member);
    }

    @Override
    public void updateMember(Long memberId, MemberUpdateDto dto) {
        repository.update(memberId, dto);
    }

    @Override
    public Member findMemberById(Long memberId) {
        return repository.findById(memberId).orElse(null);
    }

    @Override
    public List<Member> findAllMembersByCond(MemberSearchCond cond) {
        return List.of();
    }

    @Override
    public void deleteMember(Long memberId) {
        repository.delete(memberId);
    }

    @Override
    public boolean isMemberExist(String email) {
        return repository.existsByEmail(email);
    }
}
