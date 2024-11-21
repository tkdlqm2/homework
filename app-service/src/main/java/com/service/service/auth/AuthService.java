package com.service.service.auth;

import com.service.dto.request.CreateUserRequestDto;
import com.service.dto.request.VerifyAdultRequestDto;
import com.service.dto.response.CreateUserResponseDto;
import com.service.dto.response.VerifyAdultResponseDto;

public interface AuthService {
    /**
     * 회원가입
     */
    CreateUserResponseDto signup(CreateUserRequestDto createUserRequestDto);

    /**
     * 성인인증 요청
     */
    VerifyAdultResponseDto verifyAdult(VerifyAdultRequestDto verifyAdultRequestDto);
}
