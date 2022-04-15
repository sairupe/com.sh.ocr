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
        String TESSDATA_PREFIX = System.getenv("TESSDATA_PREFIX");
        log.info("ENV : TESSDATA_PREFIX : {}", TESSDATA_PREFIX);
        // 创建实例
        ITesseract instance = new Tesseract();
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎
        instance.setOcrEngineMode(1);
        // 读取文件
        // DEBUG
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode.jpg");
        // JAR
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("/sample/healthyCode.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到健康码文件");
        }
        BufferedImage image = ImageIO.read(healthyCodeIs);
        // 识别
        String result = instance.doOCR(image);
        return result;
    }

    /**
     * setOcrEngineMode = 0
     * healthyCode.jpg
     *
     * 袁屋亘熹圃骗‖x 畲 K〕S 侧笆唧29% 〔画l4二32
     * 〈 广西健康码 ′" @
     * 钟* 郗
     * 2022_04_14 16:32:00
     * 绿码
     * 回 ."垣" 回
     * 鹊 黜 '者鼻-l '
     * I_ …" _:`害i『 更!
     * ..梧_蜚辈{n
     * 回 ' 」 一
     * 家人健康码 ` 扫码入场 ′ 申诉反馈
     * 喧 核酸结果 〉 畹 行程信息 〉
     * 近2天无核酸报告 立即核验〉
     * 2022_03_26 48小时未核验
     * g 幢 ()) 】*
     * _键直报 口岸入境 通知公告 个人中心
     * @ 醴 熨 EI
     * 12345热线 健康打卡 疫苗信息 场所注册
     * ..
     *
     */


    /**
     * setOcrEngineMode = 1
     * healthyCode.jpg
     *
     * 师 晓 棣 动 仪 | 国 G Dt29% 4:32
     * 〈 广 西 健 康 码 … @
     * E
     * 2022-04-14 16:32:00
     * 绿 码
     * 团 沥 回
     * 吊 :
     * 河 z
     * 吊 浩
     * 国 仁 r
     * 家 人 健 康 码 | “ 扫 码 入 场 | “ 申 诉 反 馈
     * 自 核 酸 结 果 > 口 行 程 信 息 >
     * 近 2 天 无 核 酸 报 告 立 即 核 验
     * 2022-03-26 48 小 时 未 核 验
     * 困 W “ 申 一
     * 一 键 直 报 “ 口 岸 入 境 “ 通 知 公 告 “ 个 人 中 心
     * 口 “ 夜 出 “ 东
     * 12345 热 线 “ 健 康 打 卡 “ 疫 苗 信 息 “ 场 所 注 册
     * 碣 缘
     */


    @Data
    static class A{
        String code;
        String msg;
    }
}
