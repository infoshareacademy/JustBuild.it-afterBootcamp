package com.jaszczurki.justbuild_it_web_portal.mapper;

import com.jaszczurki.justbuild_it_web_portal.dto.UserAuthDto;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {

    public UserAuthDto toDto(User user) {
        return UserAuthDto.builder()
                .dtoUserId(user.getUserId())
                .dtoUsername(user.getUsername())
                .dtoPassword(user.getPassword())
                .dtoAuthorities(user.getAuthorities())
                .build();
    }

    public User fromDto(UserAuthDto userAuthDto) {
        return User.builder()
                .userId(userAuthDto.getDtoUserId())
                .username(userAuthDto.getDtoUsername())
                .password(userAuthDto.getDtoPassword())
                .authorities(userAuthDto.getDtoAuthorities())
                .build();
    }
}
