package com.colour.member.api.member.repository;

import com.colour.member.api.member.domain.dto.MemberSearchCond;
import com.colour.member.api.member.domain.dto.MemberUpdateDto;
import com.colour.member.api.member.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    List<Member> findAll(MemberSearchCond memberSearchCond);

    void update(Long memberId, MemberUpdateDto updateDto);

    void delete(Long memberId);

    boolean existsByEmail(String email);
}
