package com.colour.member.controller;

import com.colour.member.api.mypage.dto.MyPageResponseDto;
import com.colour.member.api.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;


@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService service;

    @GetMapping("/mypage")
    public MyPageResponseDto getMyPage() {
        return service.getMyPage(getCurrentMemberId());
    }

    @GetMapping("/mypage/{memberId}")
    public MyPageResponseDto getMyPageByMemberId(@PathVariable Long memberId) {
        return service.getMyPage(memberId);
    }
}
