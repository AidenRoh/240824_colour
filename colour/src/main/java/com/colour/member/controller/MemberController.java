package com.colour.member.controller;

import com.colour.mail.service.MailService;
import com.colour.member.api.member.domain.dto.MemberRegisterDto;
import com.colour.member.api.member.domain.dto.MemberUpdateDto;
import com.colour.member.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/signUp")
    public ResponseEntity<String> signup() {
        return new ResponseEntity<>("new-member", HttpStatus.OK);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register() {
        return new ResponseEntity<>("member-register", HttpStatus.OK);
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateMember() {
        return new ResponseEntity<>("update-member", HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signup(@ModelAttribute MemberRegisterDto memberRegisterDto,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        //check existing Member
        if (memberService.isMemberExist(memberRegisterDto.getEmail())) {
            response.sendRedirect("http://localhost:8080/login");
            return new ResponseEntity<>("redirect: login", HttpStatus.TEMPORARY_REDIRECT);
        }
        //set authorizing number by email
        String authCode = mailService.sendAuthenticationMail(memberRegisterDto.getEmail());
        HttpSession session = request.getSession();
        session.setAttribute("EMAIL_AUTH_CODE", authCode);
        session.setAttribute("SIGN_UP_FORM", memberRegisterDto);
        session.setMaxInactiveInterval(2 * 60);
        //cookie response
        response.sendRedirect("http://localhost:8080/member/register");
        return new ResponseEntity<>("redirect: register", HttpStatus.TEMPORARY_REDIRECT);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String authCode, HttpServletRequest request) {
        //get-values
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("session is null");
        }
        String storedCode = (String) session.getAttribute("EMAIL_AUTH_CODE");
        MemberRegisterDto storedDto = (MemberRegisterDto) session.getAttribute("SIGN_UP_FORM");
        //logic
        if (authCode.equals(storedCode)) {
            memberService.registerMember(storedDto);
        }
        return new ResponseEntity<>("member registered", HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateMember(@ModelAttribute("MemberUpdateDto") MemberUpdateDto dto) {
        memberService.updateMember(getCurrentMemberId(), dto);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/getMembers")
    public ResponseEntity<String> getMembers() {
        System.out.println("called");
        return new ResponseEntity<>("get-members", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember() {
        memberService.deleteMember(getCurrentMemberId());
        return new ResponseEntity<>("delete-form", HttpStatus.NO_CONTENT);
    }
}
