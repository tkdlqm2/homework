package com.service.core.util;

import java.net.URI;
import java.util.List;

public class HttpUtils {
    public static URI getUri(String url) {
        return URI.create(url);
    }

    public static JsonRpcRequest getJsonRpcRequest() {
        return JsonRpcRequest.builder()
                .id(1)
                .build();
    }

    public static JsonRpcRequest getJsonRpcRequest(String method) {
        JsonRpcRequest jsonRpcRequest = getJsonRpcRequest();
        jsonRpcRequest.setMethod(method);
        return jsonRpcRequest;
    }

    public static JsonRpcRequest getJsonRpcRequest(String method, List paramList) {
        JsonRpcRequest jsonRpcRequest = getJsonRpcRequest(method);
        jsonRpcRequest.setParams(paramList);
        return jsonRpcRequest;
    }
}
