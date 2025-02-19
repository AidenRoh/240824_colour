package com.colour.session.service;

import com.colour.member.domain.dto.MemberRegisterDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailSessionServiceImpl implements EmailSessionService {

//    @Autowired
//    private EmailSessionRepository emailSessionRepository;

    @Override
    public void createSession(MemberRegisterDto dto, String authCode) {
//        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
//        authentication.
    }

    @Override
    public String findAuthCode(String email) {
//        return emailSessionRepository.findAuthCode(email);
        return null;
    }

    @Override
    public MemberRegisterDto getMemberRegisterDto(String email) {
//        return emailSessionRepository.getMemberRegisterDto(email);
        return null;
    }

    @Override
    public void deleteSession(String email) {
//        emailSessionRepository.deleteSession(email);
    }
}
