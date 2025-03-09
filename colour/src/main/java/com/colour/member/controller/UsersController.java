package com.colour.member.controller;

import com.colour.member.api.users.dto.UsersResponseDto;
import com.colour.member.api.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;


@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {

    private final UsersService service;

    @GetMapping
    public UsersResponseDto getUsers() {
        return service.getSummaryUserPage(getCurrentMemberId());
    }

    @GetMapping("/{memberId}")
    public UsersResponseDto getUsersByMemberId(@PathVariable Long memberId) {
        return service.getSummaryUserPage(memberId);
    }

    @GetMapping("/{memberId}")
    public
}
