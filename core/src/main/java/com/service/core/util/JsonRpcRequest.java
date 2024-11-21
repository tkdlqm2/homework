package com.service.core.util;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@NoArgsConstructor
public class JsonRpcRequest {

    private String jsonrpc = "2.0";
    private String method;
    private Object params;
    private int id;

    @Builder
    public JsonRpcRequest(String method, Object params, int id) {
        this.method = method;
        this.params = params;
        this.id = id;
    }
}
