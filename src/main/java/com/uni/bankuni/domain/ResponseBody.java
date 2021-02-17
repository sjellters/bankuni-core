package com.uni.bankuni.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody<T> {

    private Date timestamp;
    private int status;
    private String error;
    private MessageResult<T> message;
    private String path;

    public static ResponseBody<Object> ok(Object content, String path) {
        return ResponseBody.builder()
                .status(HttpStatus.OK.value())
                .message(new MessageResult<>(content))
                .path(path)
                .build();
    }

    public static ResponseBody<Object> created(Object content, String path) {
        return ResponseBody.builder()
                .status(HttpStatus.CREATED.value())
                .message(new MessageResult<>(content))
                .path(path)
                .build();
    }

    public static ResponseBody<Object> badRequest(String errorMessage, String path) {
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(new MessageResult<>(errorMessage))
                .path(path)
                .build();
    }

    public static ResponseBody<Object> unauthorized(String errorMessage, String path) {
        return ResponseBody.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(new MessageResult<>(errorMessage))
                .path(path)
                .build();
    }

    public static ResponseBody<Object> internalServerError(String errorMessage, String path) {
        return ResponseBody.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(new MessageResult<>(errorMessage))
                .path(path)
                .build();
    }

    @PostConstruct
    public void init() {
        timestamp = new Date();
    }
}
