package com.colour.member.api.mypage.service;

import com.colour.member.api.mypage.dto.MyPageResponseDto;

public interface MyPageService {

    MyPageResponseDto getMyPage(Long memberId);

}
