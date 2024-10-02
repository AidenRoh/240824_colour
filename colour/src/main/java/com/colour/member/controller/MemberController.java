package com.colour.member.controller;

import com.colour.mail.MailService;
import com.colour.member.dto.MemberRegisterDto;
import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import com.colour.member.service.MemberService;
import com.colour.member.service.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
@RequestMapping("/member/test")
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;
    final MailService mailService;
    private ThreadLocal<MemberRegisterDto> register = new ThreadLocal<>();

    @GetMapping("/new-member")
    public String newMember() {
        return "ok";
    }

    @PostMapping("/new-member")
    public void newMember(@RequestParam String email, HttpServletResponse response) throws IOException {
        //check existing Member
        boolean existMember = memberService.isMemberExist(email);
        if (existMember) {
            response.sendRedirect("http://localhost:8080/login");
        }
        //set authorizing number by email
        MemberRegisterDto dto = new MemberRegisterDto();
        int createdNumber = mailService.sendMail(email);
        dto.setEmail(email);
        dto.setAuthNumber(createdNumber);
        register.set(dto);
        response.sendRedirect("http://localhost:8080/member/test/member-register");
    }

    @GetMapping("/member-register")
    public String memberRegister() {
        return "member register ok";
    }

    @PostMapping("/member-register")
    public String memberRegister(MemberRegisterDto dto) {
        //check auth Number
        if (register.get().getAuthNumber().equals(dto.getAuthNumber())) {
            return "인증번호가 맞지 않습니다.";
        }

        Member member = new Member(dto.getUsername(), dto.getPassword(), dto.getEmail());
        memberService.registerMember(member);
        return "register success";
    }

    @PostMapping("/edit-member/{member_id}")
    public String editMember(@ModelAttribute("MemberUpdateDto") MemberUpdateDto dto,
                             @PathVariable("member_id") Long member_id) {
        memberService.updateMember(member_id, dto);
        return "ok";
    }

    @GetMapping("/get-members")
    public String getMembers() {
        System.out.println("called");
        return "get-members";
    }

    @DeleteMapping("/delete-member")
    public String deleteMember() {
        System.out.println("called");
        return "delete-form";
    }
}
