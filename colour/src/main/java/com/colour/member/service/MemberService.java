package com.colour.member.service;

import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.member.domain.dto.MemberSearchCond;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.domain.entity.Member;

import java.util.List;

public interface MemberService {

    Member registerMember(MemberRegisterDto dto);

    void updateMember(Long memberId, MemberUpdateDto dto);

    Member findMemberById(Long memberId);

    List<Member> findAllMembersByCond(MemberSearchCond cond);

    Member findMemberByEmail(String email);

    void deleteMember(Long memberId);

    boolean isMemberExist(String email);
}
