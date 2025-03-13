package com.colour.member.api.member.service;

import com.colour.member.api.member.domain.dto.MemberRegisterDto;
import com.colour.member.api.member.domain.dto.MemberSearchCond;
import com.colour.member.api.member.domain.dto.MemberUpdateDto;
import com.colour.member.api.member.domain.entity.Member;

import java.util.List;

public interface MemberService {

    Member registerMember(MemberRegisterDto dto);

    void updateMember(Long memberId, MemberUpdateDto dto);

    Member findById(Long memberId);

    Member findByEmail(String email);

    Member findByUsername(String username);

    List<Member> findAllMembersByCond(MemberSearchCond cond);

    void deleteMember(Long memberId);

    boolean isMemberExist(String email);
}
