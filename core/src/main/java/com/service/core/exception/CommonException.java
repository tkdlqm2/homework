package com.service.core.exception;

import com.service.core.http.HttpMessage;
import com.service.core.http.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CommonException extends RuntimeException{
    @Getter
    protected HttpStatus httpStatus;
    @Getter
    protected String message;

    public CommonException() {
        this(ErrorCode.UNKNOWN_SERVER_ERROR);
    }

    public CommonException(Exception e, HttpStatus status) {
        super(e);
        setMessage(ErrorCode.of(status));
    }

    public CommonException(Exception e, Message error) {
        super(e);
        setMessage(error);
    }

    public CommonException(Message error) {
        super(error.getMessage());
        setMessage(error);
    }

    public CommonException(Message error, String errorMessage) {
        super();
        setCustomMessage(error, errorMessage);
    }

    public CommonException(Message error, HttpStatus status) {
        super(error.getMessage());
        setMessage(ErrorCode.of(status));
    }

    public CommonException(Throwable t) {
        super(t);
        setMessage(ErrorCode.UNKNOWN_SERVER_ERROR);
    }


    private void setMessage(Message error) {
        if (ObjectUtils.isEmpty(this.message)) {
            this.message = error.getMessage();
        }
        if (error instanceof HttpMessage) {
            if (ObjectUtils.isEmpty(this.httpStatus)) {
                this.httpStatus = ((HttpMessage) error).getHttpStatus();
            }
        }
    }

    private void setCustomMessage(Message error, String errorMessage) {
        this.message = errorMessage;
        if (error instanceof HttpMessage) {
            if (ObjectUtils.isEmpty(this.httpStatus)) {
                this.httpStatus = ((HttpMessage) error).getHttpStatus();
            }
        }
    }
}
