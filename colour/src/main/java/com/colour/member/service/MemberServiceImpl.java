package com.colour.member.service;

import com.colour.member.domain.MemberMapper;
import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.member.domain.dto.MemberSearchCond;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.domain.entity.Member;
import com.colour.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final MemberMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member registerMember(MemberRegisterDto dto) {
        MemberRegisterDto memberDto = encodeDto(dto);
        Member member = mapper.memberRegisterDtoToEntity(memberDto);
        return repository.save(member);
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
    public Member findMemberByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void deleteMember(Long memberId) {
        repository.delete(memberId);
    }

    @Override
    public boolean isMemberExist(String email) {
        return repository.existsByEmail(email);
    }

    private MemberRegisterDto encodeDto(MemberRegisterDto dto) {
        String encodingPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodingPassword);
        return dto;
    }
}
