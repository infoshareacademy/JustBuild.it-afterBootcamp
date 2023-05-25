package com.jaszczurki.justbuild_it_web_portal.mapper;

import com.jaszczurki.justbuild_it_web_portal.dto.UserAuthDto;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {

    public User fromDto(UserAuthDto userAuthDto) {
        return User.builder()
                .userId(userAuthDto.getDtoUserId())
                .username(userAuthDto.getDtoUsername())
                .password(userAuthDto.getDtoPassword())
                .authorities(userAuthDto.getDtoAuthorities())
                .firstName(userAuthDto.getDtoFirstName())
                .lastName(userAuthDto.getDtoLastName())
                .company(userAuthDto.getDtoCompany())
                .emailAddress(userAuthDto.getDtoEmail())
                .telephoneNumber(userAuthDto.getDtoPhoneNumber())
                .build();
    }
}
