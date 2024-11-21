package com.service.core.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JsonRpcError {
    private int code;
    private String message;
    private Object data;

    @Builder
    public JsonRpcError(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
