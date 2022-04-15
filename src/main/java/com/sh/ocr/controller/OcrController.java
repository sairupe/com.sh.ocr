package com.sh.ocr.controller;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;


@Slf4j
@RestController
@RequestMapping("/ocr")
public class OcrController {

    @GetMapping("/test")
    public String keys() {
        A a = new A();
        a.setCode("200");
        a.setMsg("绿码");
        String result = JSON.toJSONString(a);
        log.error("error :{} ", result);
        return result;
    }

    @GetMapping("/healthyCode")
    public String healthyCode() throws Exception {
        // 创建实例
        ITesseract instance = new Tesseract();
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎
        instance.setOcrEngineMode(1);
        // 读取文件
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("/sample/healthyCode.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到健康码文件");
        }
        BufferedImage image = ImageIO.read(healthyCodeIs);
        // 识别
        String result = instance.doOCR(image);
        return result;
    }

    @Data
    static class A{
        String code;
        String msg;
    }
}
