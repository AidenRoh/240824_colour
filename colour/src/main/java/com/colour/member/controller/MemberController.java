package com.colour.member.controller;

import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.facade.MemberFacadeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/member/test")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacadeService facadeService;

    @GetMapping("/new-member")
    public String newMember() {
        return "new-member";
    }

    @PostMapping("/new-member")
    public void newMember(@ModelAttribute("MemberRegisterDto") MemberRegisterDto dto,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        //check existing Member
        if (facadeService.checkExistsMember(dto)) {
            response.sendRedirect("http://localhost:8080/member/test/new-member");
            return;
        }
        //set authorizing number by email
        facadeService.sendAuthenticationMail(dto, request);
        //cookie response
        Cookie cookie = new Cookie("email", dto.getEmail());
        response.addCookie(cookie);
        response.sendRedirect("http://localhost:8080/member/test/member-register");
    }

    @GetMapping("/member-register")
    public String memberRegister() {
        return "member-register";
    }

    @PostMapping("/member-register")
    public String memberRegister(@RequestParam String authcode, HttpServletRequest request) {
        boolean authCheck = facadeService.checkAuthenticationCode(authcode, request);
        if (authCheck) {
            facadeService.registerMember(request);
        }
        return "ok";
    }

    @GetMapping
    public String updateMember() {
        return "update-member";
    }

    @PatchMapping("/update-member/{member_id}")
    public String updateMember(@ModelAttribute("MemberUpdateDto") MemberUpdateDto dto,
                               @PathVariable("member_id") Long member_id) {
        facadeService.updateMember(dto, member_id);
        return "ok";
    }

    @GetMapping("/get-members")
    public String getMembers() {
        System.out.println("called");
        return "get-members";
    }

    @DeleteMapping("/delete-member/{member_id}")
    public String deleteMember(@PathVariable("member_id") Long member_id) {
        facadeService.deleteMember(member_id);
        return "delete-form";
    }
}
