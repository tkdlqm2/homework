package com.service.core.http;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int status;
    protected String message;
    protected T data;
    @Builder
    public CommonResponse(HttpStatus httpStatus, String message, T data) {
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }
}
