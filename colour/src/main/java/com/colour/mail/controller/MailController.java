package com.colour.mail.controller;

import com.colour.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    final MailService mailService;

    @PostMapping("/auth-mail")
    public String sendMail(String mail) {
        int authNumber = mailService.sendMail(mail);
        return String.valueOf(authNumber);
    }
}
