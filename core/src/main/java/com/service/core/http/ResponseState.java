package com.service.core.http;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

public enum ResponseState {
    RES_CODE_SUCCESS("0000"),
    RES_HTTPCODE_SUCCESS("200"),
    RES_CODE_INVALID_PARAMETER("1000"),
    RES_CODE_SERVICE("6000"),
    RES_CODE_WARNING("9000"),
    RES_CODE_FILE_EXCEPTION("9997"),
    RES_CODE_DB_EXCEPTION("9998"),
    RES_CODE_PDO_EXCEPTION("9999");


    @JsonIgnore
    private final String codeValue;

    @JsonIgnore
    ResponseState(String s) {
        this.codeValue = s;
    }

    @JsonIgnore
    public String getCodeValue() {
        return this.codeValue;
    }

    public static String getEnum(ResponseState responseState) {
        return responseState.codeValue;
    }

    public static ResponseState of(String value) {
        return Arrays.stream(values())
                .filter(v -> value.equals(v.codeValue))
                .findFirst()
                .get();
    }
}
