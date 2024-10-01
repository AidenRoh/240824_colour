package com.colour.member.repository;

import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Member create(Member member);
    Optional<Member> findById(Long memberId);
    Member findAll();
    void update(Long memberId, MemberUpdateDto updateDto);
    void delete(Long memberId);
}
