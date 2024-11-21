package com.service.core.domain.comic;
import io.micrometer.common.util.StringUtils;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@EqualsAndHashCode  // Redis Set에서 사용될 수 있으므로 추가
public class ComicViewRequest {
    private Long comicId;
    private Long userId;
    private String deviceInfo;
    private String ipAddress;
    private LocalDate viewDate;  // 일별 조회수 관리를 위해 추가
    private String viewHash;

    @Builder
    public ComicViewRequest(Long comicId, Long userId, String deviceInfo, String ipAddress, LocalDate viewDate, String viewHash) {
        this.comicId = comicId;
        this.userId = userId;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.viewDate = viewDate;
        this.viewHash = viewHash;
    }

    /**
     * 조회 해시 생성
     */
    public String generateViewHash() {
        String combined = String.format("%d:%d:%s:%s:%s",
                this.comicId,
                this.userId,
                this.deviceInfo,
                this.ipAddress,
                this.viewDate.format(DateTimeFormatter.ISO_DATE)
        );
        return DigestUtils.sha256Hex(combined);
    }


    /**
     * 기본 생성자는 private으로 제한
     */
    private ComicViewRequest() {
        this.viewDate = LocalDate.now();  // 기본값으로 현재 날짜 설정
    }

    /**
     * Builder 패턴에서 자동으로 현재 날짜 설정
     */
    public static class ComicViewRequestBuilder {
        private LocalDate viewDate = LocalDate.now();  // Builder에서 기본값 설정
    }

    /**
     * 유효성 검증
     */
    public void validate() {
        if (comicId == null || comicId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 만화 ID입니다.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (StringUtils.isEmpty(deviceInfo)) {
            throw new IllegalArgumentException("디바이스 정보는 필수입니다.");
        }
        if (StringUtils.isEmpty(ipAddress)) {
            throw new IllegalArgumentException("IP 주소는 필수입니다.");
        }
        if (viewDate == null) {
            throw new IllegalArgumentException("조회 날짜는 필수입니다.");
        }
    }
}
