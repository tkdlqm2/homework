package com.service.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestHandler {
    private static final String UNKNOWN = "unknown";

    private static final String SUCCESS = "success";

    public static ResponseEntity<?> makeSuccessResponse() {
        return makeSuccessResponse(SUCCESS);
    }

    public static ResponseEntity<?> makeSuccessResponse(Object response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static String requestParser(Object obj) throws JsonProcessingException {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(obj);
    }

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        return ip;
    }

    private RestHandler() throws IllegalAccessException {
        throw new IllegalAccessException("RestHandler utility class");
    }
}
