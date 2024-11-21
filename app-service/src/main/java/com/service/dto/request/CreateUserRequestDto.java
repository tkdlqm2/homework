package com.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreateUserRequestDto {
    @Email
    @NotBlank
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @JsonProperty(value = "password", required = true)
    private String password;

    @Builder
    public CreateUserRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}