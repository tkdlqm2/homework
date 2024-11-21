package com.service.service.auth.impl;

import com.service.core.enums.UserStatus;
import com.service.database.repository.user.UserRepository;
import com.service.dto.request.CreateUserRequestDto;
import com.service.dto.request.VerifyAdultRequestDto;
import com.service.dto.response.CreateUserResponseDto;
import com.service.dto.response.VerifyAdultResponseDto;
import com.service.exception.user.UserErrorCode;
import com.service.exception.user.UserException;

import com.service.service.auth.AuthService;
import com.service.utils.valid.EmailValidator;
import lombok.RequiredArgsConstructor;
import com.service.database.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    @Transactional
    @Override
    public CreateUserResponseDto signup(CreateUserRequestDto createUserRequestDto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(createUserRequestDto.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        // 이메일 형식 검증
        if (!emailValidator.isValidEmail(createUserRequestDto.getEmail())) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 패스워드 암호화 및 유저 생성
        String encodedPassword = passwordEncoder.encode(createUserRequestDto.getPassword());
        User user = User.builder()
                .email(createUserRequestDto.getEmail())
                .status(UserStatus.ACTIVE)
                .lastLoginAt(LocalDateTime.now())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return CreateUserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    @Transactional
    public VerifyAdultResponseDto verifyAdult(VerifyAdultRequestDto verifyAdultRequestDto) {
        User user = userRepository.findById(verifyAdultRequestDto.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        user.authenticateAdult();
        userRepository.save(user);
        return VerifyAdultResponseDto.builder().result(true).build();
    }
}
