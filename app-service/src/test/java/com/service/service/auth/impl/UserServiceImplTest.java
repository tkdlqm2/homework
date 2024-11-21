package com.service.service.auth.impl;

import com.service.core.enums.UserStatus;
import com.service.database.entity.user.User;
import com.service.database.repository.user.UserRepository;
import com.service.dto.request.CreateUserRequestDto;
import com.service.dto.response.CreateUserResponseDto;
import com.service.exception.user.UserErrorCode;
import com.service.exception.user.UserException;
import com.service.utils.valid.EmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signup_Success() {
        // given
        String email = "test@example.com";
        String password = "password123!";
        String encodedPassword = "encodedPassword";

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        User savedUser = User.builder()
                .email(email)
                .status(UserStatus.ACTIVE)
                .password(encodedPassword)
                .build();

        ReflectionTestUtils.setField(savedUser, "id", 1L);

        when(emailValidator.isValidEmail(email)).thenReturn(true);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        CreateUserResponseDto response = authService.signup(createUserRequestDto);

        // then
        assertNotNull(response);
        assertEquals(email, response.getEmail());
        assertEquals(UserStatus.ACTIVE, response.getUserStatus());

        verify(emailValidator).isValidEmail(email);
        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_Failed_DuplicateEmail() {
        // given
        String email = "test@example.com";
        String password = "password123!";
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        UserException exception = assertThrows(UserException.class,
                () -> authService.signup(createUserRequestDto));


        assertEquals(UserErrorCode.DUPLICATE_EMAIL, exception.getCommonErrorCodeType());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 이메일 형식")
    void signUp_Failed_InvalidEmail() {
        // given
        String invalidEmail = "invalid-email";
        String password = "password123!";
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .email(invalidEmail)
                .password(password)
                .build();

        when(emailValidator.isValidEmail(invalidEmail)).thenReturn(false);

        // when & then
        UserException exception = assertThrows(UserException.class,
                () -> authService.signup(createUserRequestDto));

        assertEquals(UserErrorCode.INVALID_EMAIL_FORMAT, exception.getCommonErrorCodeType());

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        // given
        String email = "test@example.com";
        String password = "password123!";
        String encodedPassword = "encodedPassword";

        LoginRequestDto request = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        // when
        LoginResponseDto response = authService.login(request);

        // then
        assertNotNull(response);
        assertEquals(email, response.getEmail());

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_Failed_UserNotFound() {
        // given
        String email = "nonexistent@example.com";
        String password = "password123!";
        LoginRequestDto request = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        UserException exception = assertThrows(UserException.class,
                () -> authService.login(request));

        assertEquals(UserErrorCode.NOT_FOUND_USER, exception.getCommonErrorCodeType());

        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Failed_InvalidPassword() {
        // given
        String email = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        LoginRequestDto request = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // when & then
        UserException exception = assertThrows(UserException.class,
                () -> authService.login(request));

        assertEquals(UserErrorCode.INVALID_PASSWORD, exception.getCommonErrorCodeType());
    }
}