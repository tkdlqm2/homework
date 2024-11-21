package com.service.dto.response;

import com.service.core.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserResponseDto {

    private Long id;
    private String email;
    private UserStatus userStatus;

    @Builder
    public CreateUserResponseDto(Long id, String email, UserStatus userStatus) {
        this.userStatus = userStatus;
        this.email = email;
        this.id = id;
    }
}
