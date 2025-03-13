package com.colour.member.controller;

import com.colour.member.api.follow.domain.dto.FollowDto;
import com.colour.member.api.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.colour.security.utils.SecurityUtils.getCurrentMemberId;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("follow")
    public ResponseEntity<String> followMember(@RequestParam long followeeId) {
        FollowDto dto = new FollowDto();
        dto.setFollowerId(getCurrentMemberId());
        dto.setFolloweeId(followeeId);
        followService.followUser(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("unfollow")
    public ResponseEntity<String> unfollowMember(@RequestParam long followeeId) {
        FollowDto dto = new FollowDto();
        dto.setFollowerId(getCurrentMemberId());
        dto.setFolloweeId(followeeId);
        followService.unfollowUser(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
