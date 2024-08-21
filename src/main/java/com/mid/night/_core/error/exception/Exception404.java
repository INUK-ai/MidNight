package com.mid.night._core.error.exception;

import com.mid.night._core.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 잘못된 경로
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}


