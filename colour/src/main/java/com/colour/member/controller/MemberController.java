package com.colour.member.controller;

import com.colour.mail.service.MailService;
import com.colour.member.dto.MemberRegisterDto;
import com.colour.member.dto.MemberUpdateDto;
import com.colour.member.entity.Member;
import com.colour.member.service.MemberService;
import com.colour.session.entity.EmailSession;
import com.colour.session.service.EmailSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/member/test")
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;
    final MailService mailService;
    final EmailSessionService emailSessionService;

    @GetMapping("/new-member")
    public String newMember() {
       return "new-member";
    }

    @PostMapping("/new-member")
    public void newMember(@ModelAttribute("MemberRegisterDto") MemberRegisterDto dto,
                          HttpServletResponse response) throws IOException {
        //check existing Member
        String email = dto.getEmail();
        boolean existMember = memberService.isMemberExist(email);
        if (existMember) {
            response.sendRedirect("http://localhost:8080/member/test/new-member");
            return;
        }

        //set authorizing number by email
        int authCode = mailService.sendMail(email);
        emailSessionService.createSession(dto, String.valueOf(authCode));

        //cookie response
        Cookie cookie = new Cookie("email", email);
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:8080/member/test/member-register");
    }

    @GetMapping("/member-register")
    public String memberRegister() {
        return "member-register";
    }

    @PostMapping("/member-register")
    public String memberRegister(@RequestParam String authcode, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String email = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("email"))
                .map(Cookie::getValue).findFirst()
                .orElse(null);
        String code = emailSessionService.findAuthCode(email);

        if (authcode.equals(code)) {
            EmailSession session = emailSessionService.findSession(email);
            Member member = new Member(session.getUsername(), session.getPassword(), session.getEmail());
            memberService.registerMember(member);
        }
//        emailSessionService.deleteSession(email);
        return "ok";
    }

    @GetMapping
    public String updateMember() {
        return "update-member";
    }

    @PatchMapping("/update-member/{member_id}")
    public String updateMember(@ModelAttribute("MemberUpdateDto") MemberUpdateDto dto,
                             @PathVariable("member_id") Long member_id) {
        memberService.updateMember(member_id, dto);
        return "ok";
    }

    @GetMapping("/get-members")
    public String getMembers() {
        System.out.println("called");
        return "get-members";
    }

    @DeleteMapping("/delete-member/{member_id}")
    public String deleteMember(@PathVariable("member_id") Long member_id) {
        if ( memberService.findMemberById(member_id) != null) {
            memberService.deleteMember(member_id);
        }
        return "delete-form";
    }
}
