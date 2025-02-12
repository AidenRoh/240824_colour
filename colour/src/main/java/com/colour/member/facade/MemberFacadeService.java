package com.colour.member.facade;

import com.colour.mail.service.MailService;
import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.member.domain.dto.MemberUpdateDto;
import com.colour.member.service.MemberService;
import com.colour.session.service.EmailSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberFacadeService {

    private final MemberService memberService;
    private final MailService mailService;
    private final EmailSessionService emailSessionService;

    public MemberFacadeService(MemberService memberService, MailService mailService, EmailSessionService emailSessionService) {
        this.memberService = memberService;
        this.mailService = mailService;
        this.emailSessionService = emailSessionService;
    }

    public boolean checkExistsMember(MemberRegisterDto dto) {
        return memberService.isMemberExist(dto.getEmail());
    }

    public void sendAuthenticationMail(MemberRegisterDto dto, HttpServletRequest request) {
        String authCode = mailService.sendAuthenticationMail(dto.getEmail());
        request.getSession().setAttribute("authCode", authCode);
        request.getSession().setAttribute("memberRegisterForm", dto);
        emailSessionService.createSession(dto, String.valueOf(authCode));
    }

    public boolean checkAuthenticationCode(String authCode, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute("authCode");
        String code = emailSessionService.findAuthCode(sessionCode);
        return authCode.equals(sessionCode);
    }

    public void registerMember(HttpServletRequest request) {
        MemberRegisterDto dto = (MemberRegisterDto) request.getSession().getAttribute("memberRegisterForm");
        memberService.registerMember(dto);
    }

    public void updateMember(MemberUpdateDto dto, Long memberId) {
        memberService.updateMember(memberId, dto);
    }

    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }
}
