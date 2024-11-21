package com.service.redis.cache;

import com.service.core.domain.comic.ComicViewRequest;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("comic_view_cache")
public class ComicViewCache implements Serializable {
    private Long comicId;
    private Long userId;
    private String deviceInfo;
    private String ipAddress;
    private LocalDate viewedDate;
    private String viewHash;

    public static ComicViewRequest from(ComicViewCache comicViewCache) {
        return ComicViewRequest.builder()
                .viewDate(comicViewCache.getViewedDate())
                .comicId(comicViewCache.getComicId())
                .deviceInfo(comicViewCache.getDeviceInfo())
                .ipAddress(comicViewCache.getIpAddress())
                .userId(comicViewCache.getUserId())
                .viewHash(comicViewCache.getViewHash())
                .build();
    }
}
