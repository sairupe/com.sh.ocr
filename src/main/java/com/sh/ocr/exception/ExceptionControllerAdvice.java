package com.sh.ocr.exception;

import com.sh.ocr.res.common.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RestResult handle(Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        RestResult result = new RestResult();
        result.setCode("500");
        result.setMsg(e.getMessage());
        result.setSucceed(false);
        return result;
    }

}