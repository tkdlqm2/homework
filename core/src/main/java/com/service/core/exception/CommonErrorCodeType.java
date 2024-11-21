package com.service.core.exception;

import org.springframework.http.HttpStatus;

public interface CommonErrorCodeType {
    String getErrorCode();
    String getMessage();
    HttpStatus getHttpStatus();


}
