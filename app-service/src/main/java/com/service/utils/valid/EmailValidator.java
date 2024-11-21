package com.service.utils.valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * 이메일 유효성 검증
     *
     * @param email 검증할 이메일 주소
     * @return 유효성 여부
     */
    public boolean isValidEmail(String email) {
        try {
            if (email == null) {
                return false;
            }

            // 기본적인 형식 검증
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                return false;
            }

            // 추가 검증
            // 1. 길이 체크
            if (email.length() > 254) {
                return false;
            }

            // 2. 로컬 파트 길이 체크
            String localPart = email.split("@")[0];
            if (localPart.length() > 64) {
                return false;
            }

            // 3. 도메인 유효성 검증
            String domain = email.split("@")[1];
            if (!isValidDomain(domain)) {
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("Error validating email: {} - {}", email, e.getMessage());
            return false;
        }
    }

    /**
     * 도메인 유효성 검증
     */
    private boolean isValidDomain(String domain) {
        // 도메인 길이 체크
        if (domain.length() > 255) {
            return false;
        }

        // 각 도메인 파트 체크
        String[] parts = domain.split("\\.");
        if (parts.length < 2) {
            return false;
        }

        // 각 파트가 하이픈으로 시작하거나 끝나지 않는지 체크
        for (String part : parts) {
            if (part.isEmpty() || part.startsWith("-") || part.endsWith("-")) {
                return false;
            }
        }

        return true;
    }

    /**
     * 이메일 정규화 (소문자 변환)
     */
    public String normalizeEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }
}
