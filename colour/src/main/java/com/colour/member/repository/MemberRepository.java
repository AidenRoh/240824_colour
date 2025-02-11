package com.colour.member.repository;

import com.colour.member.domain.dto.MemberSearchCond;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.domain.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository {

    Member create(Member member);
    Optional<Member> findById(Long memberId);
    List<Member> findAll(MemberSearchCond memberSearchCond);
    void update(Long memberId, MemberUpdateDto updateDto);
    void delete(Long memberId);
    boolean existsByEmail(String email);
}
