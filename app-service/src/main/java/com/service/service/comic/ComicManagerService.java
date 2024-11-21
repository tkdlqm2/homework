package com.service.service.comic;

import com.service.dto.request.CreateComicRequestDto;
import com.service.dto.request.DeleteComicRequestDto;
import com.service.dto.request.UpdateComicRequestDto;
import com.service.dto.response.CreateComicResponseDto;
import com.service.dto.response.DeleteComicResponseDto;
import com.service.dto.response.UpdateComicResponseDto;

public interface ComicManagerService {

    /**
     * 작품 생성
     */

    CreateComicResponseDto createComic(CreateComicRequestDto createComicRequestDto);

    /**
     * 작품 삭제
     */
    DeleteComicResponseDto deleteComic(DeleteComicRequestDto deleteComicRequestDto);

    /**
     * 작품 상태 변경 (이벤트 -> 유료/무료)
     */
    UpdateComicResponseDto updateComicStatus(UpdateComicRequestDto updateComicRequestDto);
}
