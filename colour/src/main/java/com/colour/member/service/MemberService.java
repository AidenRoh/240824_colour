package com.colour.member.service;

import com.colour.member.dto.MemberRegisterDto;
import com.colour.member.dto.MemberSearchCond;
import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;

import java.util.List;

public interface MemberService {

    Member registerMember(MemberRegisterDto dto);
    void updateMember(Long memberId, MemberUpdateDto dto);
    Member findMemberById(Long memberId);
    List<Member> findAllMembersByCond(MemberSearchCond cond);
    void deleteMember(Long memberId);
}
