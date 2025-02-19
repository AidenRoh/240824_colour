package com.colour.security.utils;

import com.colour.member.domain.dto.MemberSecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    public static Long getCurrentMemberId() {
        Authentication authToken = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        if (authToken == null || !authToken.isAuthenticated()) {
            throw new IllegalStateException("Authentication is required to access this resource");
        }
        MemberSecurityDto dto = (MemberSecurityDto) authToken.getPrincipal();
        return dto.getMemberId();
    }
}
