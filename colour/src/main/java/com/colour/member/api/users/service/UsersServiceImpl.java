package com.colour.member.api.users.service;

import com.colour.member.api.users.dto.UsersResponseDto;
import com.colour.member.api.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;

    public UsersServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsersResponseDto getSummaryUserPage(Long memberId) {
        UsersResponseDto responseDto = repository.getCountableInfo(memberId);
        responseDto.setPosts(repository.getSummaryPosts(memberId));
        responseDto.setHashtags(repository.getSummaryHashtags(memberId));
        responseDto.setColors(repository.getSummaryColors(memberId));
        return responseDto;
    }
}
