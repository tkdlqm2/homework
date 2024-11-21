package com.service.core.http;

import com.service.core.exception.CommonException;
import org.springframework.http.HttpStatus;

public class CommonResponseFactory {
    public static <T> CommonResponse createSuccess() {
        return CommonResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .message(ResponseCode.OK.getMessage())
                .build();
    }
    public static <T> CommonResponse createSuccess(T data) {
        return CommonResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .message(ResponseCode.OK.getMessage())
                .data(data)
                .build();
    }
    public static <T> CommonResponse createError(CommonException e) {
        return CommonResponse.<T>builder()
                .httpStatus(e.getHttpStatus())
                .message(e.getMessage())
                .build();
    }
    public static <T> CommonResponse createError(Exception e, HttpStatus httpStatus) {
        return createError(new CommonException(e, httpStatus));
    }

    public static <T> CommonResponse createFatalError(Exception e) {
        CommonException exception = new CommonException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        return createError(exception);
    }

    public static <T> CommonResponse createValidationError() {
        return null;
    }

}
