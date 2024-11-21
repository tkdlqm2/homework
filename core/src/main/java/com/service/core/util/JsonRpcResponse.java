package com.service.core.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JsonRpcResponse<T> {
    private int id;
    private T result;
    private JsonRpcError error;

    @Builder
    public JsonRpcResponse(int id, T result, JsonRpcError error) {
        this.id = id;
        this.result = result;
        this.error = error;
    }
}
