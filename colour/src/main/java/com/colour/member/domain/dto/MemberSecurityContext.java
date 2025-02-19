package com.colour.member.domain.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class MemberSecurityContext implements UserDetails {

    private final List<GrantedAuthority> authorities;
    private final MemberSecurityDto dto;

    public MemberSecurityContext(List<GrantedAuthority> authorities, MemberSecurityDto dto) {
        this.authorities = authorities;
        this.dto = dto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return dto.getPassword();
    }

    @Override
    public String getUsername() {
        return dto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
