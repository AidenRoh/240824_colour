package com.colour.member.api.mypage.service;

import com.colour.member.api.mypage.dto.MyPageResponseDto;
import com.colour.member.api.mypage.repository.MyPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyPageServiceImpl implements MyPageService {

    private final MyPageRepository repository;

    public MyPageServiceImpl(MyPageRepository repository) {
        this.repository = repository;
    }

    @Override
    public MyPageResponseDto getMyPage(Long memberId) {
        MyPageResponseDto responseDto = repository.getCountableInfo(memberId);
        responseDto.setPosts(repository.getPosts(memberId));
        responseDto.setHashtags(repository.getHashtags(memberId));
        responseDto.setColors(repository.getColors(memberId));
        return responseDto;
    }
}
