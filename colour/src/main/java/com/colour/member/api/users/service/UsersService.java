package com.colour.member.api.users.service;

import com.colour.member.api.users.dto.UsersResponseDto;

public interface UsersService {

    UsersResponseDto getSummaryUserPage(Long memberId);

}
