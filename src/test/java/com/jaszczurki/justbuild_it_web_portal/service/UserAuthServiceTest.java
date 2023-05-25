package com.jaszczurki.justbuild_it_web_portal.service;

import com.jaszczurki.justbuild_it_web_portal.dto.UserAuthDto;
import com.jaszczurki.justbuild_it_web_portal.entity.User;
import com.jaszczurki.justbuild_it_web_portal.mapper.UserAuthMapper;
import com.jaszczurki.justbuild_it_web_portal.repository.UserAuthRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAuthServiceTest {

    private final static String TEST_USERNAME_ONE = "energoInstall#1";
    private final static String TEST_PASSWORD_ONE = "eNErgo111#";
    private final static String TEST_ROLE_ONE = "USER";
    private final static String TEST_USERNAME_TWO = "jankoMuzykant87";
    private final static String TEST_PASSWORD_TWO = "J@nek87!";
    private final static String TEST_ROLE_TWO = "USER";

    private UserAuthService userService;
    private UserAuthRepository userRepositoryMock;
    private BCryptPasswordEncoder bCryptPasswordEncoderMock;
    private UserAuthMapper authMapperMock;
    private static UserAuthDto user1;
    private static User user2;

    @BeforeAll
    static void setUpTest() {
        user1 = UserAuthDto.builder()
                .dtoUsername(TEST_USERNAME_ONE)
                .dtoPassword(TEST_PASSWORD_ONE)
                .dtoAuthorities(Set.of(new SimpleGrantedAuthority(TEST_ROLE_ONE)))
                .build();
        user2 = User.builder()
                .username(TEST_USERNAME_TWO)
                .password(TEST_PASSWORD_TWO)
                .authorities(Set.of(new SimpleGrantedAuthority(TEST_ROLE_TWO)))
                .build();
    }

    @AfterAll
    static void closeTest() {
        user1 = null;
        user2 = null;
    }

    @BeforeEach
    void setUp() {
        userRepositoryMock = mock(UserAuthRepository.class);
        bCryptPasswordEncoderMock = mock(BCryptPasswordEncoder.class);
        authMapperMock = mock(UserAuthMapper.class);
        userService = new UserAuthService(userRepositoryMock, bCryptPasswordEncoderMock,authMapperMock);
    }


    @Test
    void testIfUserIsProperlyAddedFromRegisterForm() {
        // given
        when(bCryptPasswordEncoderMock.encode(TEST_PASSWORD_ONE)).thenReturn(TEST_PASSWORD_ONE);
        ArgumentCaptor<UserAuthDto> userCaptor = ArgumentCaptor.forClass(UserAuthDto.class);
        when(authMapperMock.fromDto(userCaptor.capture())).thenAnswer(invocationOnMock -> {
            UserAuthDto userDto = userCaptor.getValue();
            User user = new User();
            user.setUserId(1L);
            user.setUsername(userDto.getDtoUsername());
            user.setPassword(userDto.getDtoPassword());
            user.setAuthorities(userDto.getDtoAuthorities());
            return user;
        });

        // when
        userService.addUserFromForm(TEST_USERNAME_ONE, TEST_PASSWORD_ONE, false, user1);

        // then
        verify(userRepositoryMock, times(1)).save(any(User.class));
        User savedUser = authMapperMock.fromDto(userCaptor.getValue());
        assertEquals(1L, savedUser.getUserId());
        assertEquals(TEST_USERNAME_ONE, savedUser.getUsername());
        assertEquals(TEST_PASSWORD_ONE, savedUser.getPassword());
        assertEquals(Set.of(new SimpleGrantedAuthority(TEST_ROLE_ONE)), savedUser.getAuthorities());
    }


    @Test
    void testIfUserIsCorrectlyFoundByEmail() {
        // given
        when(userRepositoryMock.findUserByUsername(TEST_USERNAME_TWO)).thenReturn(user2);

        // when
        User userFoundByEmail = userService.findUserByLogin(TEST_USERNAME_TWO);

        // then
        verify(userRepositoryMock, times(1)).findUserByUsername(TEST_USERNAME_TWO);
        assertThat(userFoundByEmail.getPassword())
                .isEqualTo(user2.getPassword())
                .isNotEqualTo(user1.getDtoPassword());
        assertThat(userFoundByEmail)
                .hasFieldOrPropertyWithValue("username", user2.getUsername())
                .hasFieldOrPropertyWithValue("authorities", Set.of(new SimpleGrantedAuthority("USER")))
                .hasNoNullFieldsOrPropertiesExcept("userId", "firstName", "lastName", "company", "emailAddress", "telephoneNumber", "offers");

    }

    @Test
    void testGetUserIdByUsername() {
        // Given
        String username = "john";
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        when(userRepositoryMock.findUserByUsername(username)).thenReturn(user);

        // When
        Long result = userService.findUserIdByUsername(username);

        // Then
        verify(userRepositoryMock, times(1)).findUserByUsername(username);
        assertEquals(userId, result);
    }
}