package com.colour.member.controller;

import com.colour.member.api.member.domain.entity.Member;
import com.colour.member.api.member.service.MemberService;
import com.colour.member.api.users.dto.Responsible;
import com.colour.member.api.users.dto.UsersResponseDto;
import com.colour.member.api.users.service.UsersService;
import com.colour.search.domain.enums.SortOption;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.colour.search.domain.enums.SortOption.LATEST;
import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;


@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UsersController {

    private final UsersService service;
    private final MemberService memberService;

    @GetMapping("/mypage")
    public UsersResponseDto getUsers(Pageable pageable) {
        return service.getSummary(getCurrentMemberId(), pageable);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsersResponseDto> getUserSummaryPage(@PathVariable String username, Pageable pageable) {
        Member user = memberService.findByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(service.getSummary(user.getMemberId(), pageable));
    }

    @GetMapping(value = "/{username}", params = "tab")
    public ResponseEntity<Page<? extends Responsible>> getUserSummaryPage(@PathVariable String username,
                                                                          @RequestParam String tab,
                                                                          @RequestParam(required = false) String sort,
                                                                          Pageable pageable) {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                SortOption.getSort((sort != null) ? sort : LATEST.name()));

        Member user = memberService.findByUsername(username);
        Page<? extends Responsible> result = Tab.getTab(tab).execute(service, user.getMemberId(), sortPageable);
        if (result.equals(Page.empty())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(username));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //internal logic
    @AllArgsConstructor
    private enum Tab {
        FOLLOWING(UsersService::getFollowees),
        FOLLOWER(UsersService::getFollowers),
        HASHTAG(UsersService::getHashtags),
        POST(UsersService::getPosts),
        DEFAULT(UsersService::getDefault);

        private final TabFunction function;

        public Page<? extends Responsible> execute(UsersService memberService, Long memberId, Pageable pageable) {
            return function.apply(memberService, memberId, pageable);
        }

        public static Tab getTab(String value) {
            try {
                return value != null ? Tab.valueOf(value.toUpperCase()) : DEFAULT;
            } catch (IllegalArgumentException e) {
                return DEFAULT;
            }
        }

        @FunctionalInterface
        public interface TabFunction {
            Page<? extends Responsible> apply(UsersService service, Long memberId, Pageable pageable);
        }
    }

}
