package com.sh.ocr.controller;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Value("${tess4j.data-path}")
    private String dataPath;


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
        String result = "200";
//        String TESSDATA_PREFIX = System.getenv("TESSDATA_PREFIX");
//        log.info("ENV : TESSDATA_PREFIX : {}", TESSDATA_PREFIX);
        log.info("ENV : dataPath : {}", dataPath);
        // 创建实例
        ITesseract instance = new Tesseract();
        // 精度
//        instance.setTessVariable("user_definded_dpi", "900");
        // 训练文件路径
        instance.setDatapath(dataPath);
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎 ITessAPI TessOcrEngineMode.class
        instance.setOcrEngineMode(0);
        // 识别图形模式？
//        instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SPARSE_TEXT);
        // 读取文件
        // DEBUG
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/12315.jpeg");
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/img.png");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode.jpg");
        // JAR
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("/sample/healthyCode.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到健康码文件");
        }
        // 图像预处理
        BufferedImage bi = ImageIO.read(healthyCodeIs);
        int biWidth = bi.getWidth();
        int biHeight = bi.getHeight();
        BufferedImage invertImg = ImageHelper.invertImageColor(bi);
        // 二值化
        BufferedImage invert2BinaryImage = ImageHelper.convertImageToBinary(invertImg);
        BufferedImage binaryImage = ImageHelper.convertImageToBinary(bi);
        // 灰度化
        BufferedImage grayImg = ImageHelper.convertImageToGrayscale(bi);
        // 处理文件写入
//        boolean writeResult = ImageIO.write(bi, "jpeg", new File("E://12315.jpeg"));
//        log.info("writeResult -----> :{}", writeResult);
        // 识别
        result = instance.doOCR(bi);
        // 取词处理, 按照每个字取词
        int pageIteratorLevel = ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE;
        List<Word> wordList = instance.getWords(bi, pageIteratorLevel);
        for (Word word : wordList) {
            log.info(word.toString());
        }
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
