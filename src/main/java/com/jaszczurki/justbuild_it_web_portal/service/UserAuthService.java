package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.dto.UserAuthDto;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import com.jaszczurki.justbuild_it_web_portal.mapper.UserAuthMapper;
import com.jaszczurki.justbuild_it_web_portal.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserAuthMapper authMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthService.class);

    public void addUserFromForm(String username, String password, boolean isAdmin, UserAuthDto userDto) {
        LOGGER.debug("Adding user from form: username='{}'", username);

        String encodedPassword = passwordEncoder.encode(password);
        User newUser = authMapper.fromDto(userDto);
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);

        if (isAdmin) {
            newUser.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
        } else {
            newUser.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("USER")));
            setAdditionalUserDetails(newUser, userDto);
        }
        userRepository.save(newUser);
    }

    public void addAdminFromForm(String username, String password, UserAuthDto userDto) {
        addUserFromForm(username, password, true, userDto);
    }

    private void setAdditionalUserDetails(User user, UserAuthDto userDto) {
        user.setFirstName(userDto.getDtoFirstName());
        user.setLastName(userDto.getDtoLastName());
        user.setCompany(userDto.getDtoCompany());
        user.setEmailAddress(userDto.getDtoEmail());
        user.setTelephoneNumber(userDto.getDtoPhoneNumber());
    }

    public User findUserByLogin(String username) {
        LOGGER.debug("Finding user by login: '{}'", username);
        return userRepository.findUserByUsername(username);
    }

    public Long findUserIdByUsername(String username) {
        LOGGER.debug("Getting user ID by username: '{}'", username);
        User user = userRepository.findUserByUsername(username);
        return user != null ? user.getUserId() : null;
    }
}
