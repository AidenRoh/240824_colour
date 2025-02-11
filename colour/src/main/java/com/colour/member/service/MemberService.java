package com.colour.member.service;

import com.colour.member.domain.dto.MemberSearchCond;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.domain.entity.Member;

import java.util.List;

public interface MemberService {

    Member registerMember(Member member);
    void updateMember(Long memberId, MemberUpdateDto dto);
    Member findMemberById(Long memberId);
    List<Member> findAllMembersByCond(MemberSearchCond cond);
    void deleteMember(Long memberId);
    boolean isMemberExist(String email);
}
