package com.service.exception;

import com.service.exception.comic.ComicException;
import com.service.exception.comicViewLog.ComicViewLogException;
import com.service.exception.comicViewStats.ComicViewStatsException;
import com.service.exception.purchase.PurchaseException;
import com.service.exception.purchaseStats.PurchaseStatsException;
import com.service.exception.user.UserException;
import com.service.redis.exception.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ldap.Rdn;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> UserStatsException(UserException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ErrorResponse> RedisException(RedisException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(PurchaseStatsException.class)
    public ResponseEntity<ErrorResponse> PurchaseStatsException(PurchaseStatsException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }
    @ExceptionHandler(PurchaseException.class)
    public ResponseEntity<ErrorResponse> PurchaseException(PurchaseException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }
    @ExceptionHandler(ComicViewStatsException.class)
    public ResponseEntity<ErrorResponse> ComicViewStatsException(ComicViewStatsException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(ComicViewLogException.class)
    public ResponseEntity<ErrorResponse> ComicViewLogException(ComicViewLogException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(ComicException.class)
    public ResponseEntity<ErrorResponse> comicException(ComicException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationException(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> "field: " + error.getField() + ", " + "error: " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse("REQUEST_VALIDATION_ERROR", "Validation 에러 입니다.", errors), HttpStatus.BAD_REQUEST);
    }
}
