package com.colour.member.repository;

import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberRepositoryJDBCTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void crud() {
        //given
        Member member = new Member("aidenRoh", "innovation", "aidenRoh@gmail.com");
        MemberUpdateDto updateDto1 = new MemberUpdateDto();
        MemberUpdateDto updateDto2 = new MemberUpdateDto();
        MemberUpdateDto updateDto3 = new MemberUpdateDto();

        //when & then
        //create
        Member createdMember = memberRepository.create(member);
        Number key = createdMember.getMemberId();
        assertThat(createdMember).isEqualTo(memberRepository.findById(key.longValue()).get());
        //update
        updateDto1.setUsername("dongjun");
        memberRepository.update(key.longValue(), updateDto1);
        Member findMember = memberRepository.findById(key.longValue()).orElseThrow();
        assertThat(findMember.getUsername()).isEqualTo("dongjun");

        updateDto2.setPassword("innovative");
        memberRepository.update(key.longValue(), updateDto2);
        Member findMember2 = memberRepository.findById(key.longValue()).orElseThrow();
        assertThat(findMember2.getPassword()).isEqualTo("innovative");

        updateDto3.setUsername("roh");
        updateDto3.setPassword("developer");
        memberRepository.update(key.longValue(), updateDto3);
        Member findMember3 = memberRepository.findById(key.longValue()).orElseThrow();
        assertThat(findMember3.getUsername()).isEqualTo("roh");
        assertThat(findMember3.getPassword()).isEqualTo("developer");

        //delete
        memberRepository.delete(key.longValue());
        assertThat(memberRepository.findById(key.longValue())).isEmpty();
    }
}