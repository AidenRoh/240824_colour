package com.colour.member.domain;

import com.colour.member.domain.dto.MemberRegisterDto;
import com.colour.member.domain.dto.MemberSecurityDto;
import com.colour.member.domain.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    // MemberRegister -> Entity
    @Mappings({
            @Mapping(target = "memberId", ignore = true),
            @Mapping(target = "role", ignore = true)
    })
    Member memberRegisterDtoToEntity(MemberRegisterDto dto);

    // MemberSecurity <- Entity
//    @Mappings({
//            @Mapping(target = "memberId", ignore = true),
//            @Mapping(target = "role", ignore = true)
//    })
//    @Mapping(target = "password", ignore = true)
    MemberSecurityDto entityToMemberSecurityDto(Member member);
}
