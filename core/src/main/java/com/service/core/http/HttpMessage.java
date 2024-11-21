package com.service.core.http;

import org.springframework.http.HttpStatus;

public interface HttpMessage extends Message {
    public HttpStatus getHttpStatus();
}
