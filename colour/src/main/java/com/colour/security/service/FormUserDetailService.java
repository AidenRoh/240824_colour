package com.colour.security.service;

import com.colour.member.domain.MemberMapper;
import com.colour.member.domain.dto.MemberSecurityContext;
import com.colour.member.domain.dto.MemberSecurityDto;
import com.colour.member.domain.entity.Member;
import com.colour.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormUserDetailService implements UserDetailsService {

    private final MemberRepository repository;
    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = repository.findByEmail(email).orElse(null);
        if (findMember == null) {
            throw new UsernameNotFoundException("No user found with this email: " + email);
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(findMember.getRole()));
        MemberSecurityDto dto = memberMapper.entityToMemberSecurityDto(findMember);
        return new MemberSecurityContext(authorities, dto);
    }
}
