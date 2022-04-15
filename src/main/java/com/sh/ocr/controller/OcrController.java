package com.sh.ocr.controller;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/ocr")
public class OcrController {

    @PostMapping("/test")
    public String keys() {
        A a = new A();
        a.setCode("200");
        a.setMsg("绿码");
        String result = JSON.toJSONString(a);
        log.error("error :{} ", result);
        return result;
    }

    @Data
    static class A{
        String code;
        String msg;
    }
}
