package com.service.controller;

import com.service.dto.request.CreateUserRequestDto;
import com.service.dto.request.VerifyAdultRequestDto;
import com.service.dto.response.CreateUserResponseDto;
import com.service.dto.response.VerifyAdultResponseDto;
import com.service.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증/인가 관련 API")
@RestController  // 추가
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "신규 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponseDto> signup(@RequestBody @Valid CreateUserRequestDto request) {
        return ResponseEntity.ok(authService.signup(request));
    }
    @Operation(summary = "성인 인증", description = "사용자의 성인 인증을 처리합니다.")
    @PostMapping("/verify-adult")
    public ResponseEntity<VerifyAdultResponseDto> verifyAdult(@RequestBody @Valid VerifyAdultRequestDto request) {
        return ResponseEntity.ok(authService.verifyAdult(request));
    }
}
