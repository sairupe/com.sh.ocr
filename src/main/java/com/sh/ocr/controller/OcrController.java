package com.sh.ocr.controller;

import com.alibaba.fastjson.JSON;
import com.sh.ocr.utils.QrcodeUtilz;
import com.sh.ocr.utils.StringUtilz;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
@RequestMapping("/ocr")
public class OcrController {

    @Value("${tess4j.data-path}")
    private String dataPath;


    @GetMapping("/test")
    public OcrRes keys() {
        OcrRes a = new OcrRes();
        a.setName("啊啊啊");
        a.setTime("2020-2");
        a.setStatus("绿码");
        String result = JSON.toJSONString(a);
        log.error("error :{} ", result);
        return a;
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
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/img.png");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode.jpg");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode2.jpg");
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode3.png");
        // JAR
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("/sample/healthyCode.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到健康码文件");
        }
        // 图像预处理
        BufferedImage bi = ImageIO.read(healthyCodeIs);
        // 剪裁
        int biWidth = bi.getWidth();
        int biHeight = bi.getHeight();
        int startX = 0;
        int startY = (int) (biHeight * 0.15f);
        int rectX = biWidth;
        int rectY = (int) (biHeight * 0.4f);
        BufferedImage subImage = ImageHelper.getSubImage(bi, startX, startY, rectX, rectY);
        BufferedImage binarySubImage = ImageHelper.convertImageToBinary(subImage);
        // 剪裁姓名区, 名字为白色不能二值化
        int nameStartX = 0;
        int nameStartY = (int) (biHeight * 0.1f);
        int nameRectX = biWidth;
        int nameRectY = (int) (biHeight * 0.05f);
        BufferedImage subNameImage = ImageHelper.getSubImage(bi, nameStartX, nameStartY, nameRectX, nameRectY);
        BufferedImage invertSubNameImage = ImageHelper.invertImageColor(subNameImage);
        // 反转颜色
        BufferedImage invertImg = ImageHelper.invertImageColor(bi);
        BufferedImage invertSubImg = ImageHelper.invertImageColor(subImage);
        // 二值化
        BufferedImage invert2BinaryImage = ImageHelper.convertImageToBinary(invertImg);
        BufferedImage binaryImage = ImageHelper.convertImageToBinary(bi);
        // 灰度化
        BufferedImage grayImg = ImageHelper.convertImageToGrayscale(bi);
        // 处理文件写入
        boolean writeResult = ImageIO.write(binarySubImage, "jpeg", new File("E://12315.jpeg"));
        log.info("writeResult -----> :{}", writeResult);
        // 取词处理, 按照每个字取词
//        int pageIteratorLevel = ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE;
//        List<Word> wordList = instance.getWords(bi, pageIteratorLevel);
//        for (Word word : wordList) {
//            log.info(word.toString());
//        }
        // 识别
        String userName = instance.doOCR(invertSubNameImage);
        String content = instance.doOCR(binarySubImage);
        result = userName + "\n" + content;
        return result;
    }


    @GetMapping("/healthyCodeV2")
    public OcrRes healthyCodeV2() throws Exception {
        OcrRes result = new OcrRes();
        log.info("ENV : dataPath : {}", dataPath);
        // 创建实例
        ITesseract instance = new Tesseract();
        // 训练文件路径
        instance.setDatapath(dataPath);
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎 ITessAPI TessOcrEngineMode.class
        instance.setOcrEngineMode(1);
        // 识别图形模式？
//        instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SPARSE_TEXT);
        // 读取文件
        // DEBUG
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/12315.jpeg");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/img.png");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode.jpg");
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode2.jpg");
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/healthyCode3.png");
        // JAR
//        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("/sample/healthyCode.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到健康码文件");
        }
        // 图像预处理
        BufferedImage bi = ImageIO.read(healthyCodeIs);
        int biWidth = bi.getWidth();
        int biHeight = bi.getHeight();
        // 剪裁姓名区, 名字为白色不能二值化
        int nameStartX = 0;
        int nameStartY = (int) (biHeight * 0.1f);
        int nameRectX = biWidth;
        int nameRectY = (int) (biHeight * 0.05f);
        BufferedImage subNameImage = ImageHelper.getSubImage(bi, nameStartX, nameStartY, nameRectX, nameRectY);
        BufferedImage binaryNameImage = ImageHelper.convertImageToBinary(subNameImage);
        BufferedImage invertSubNameImage = ImageHelper.invertImageColor(subNameImage);
        // 识别用户名
        String userName = instance.doOCR(invertSubNameImage);
        userName = StringUtilz.extractHealthyNameInfo(userName);
        result.setName(userName);
        // 剪裁截图时间和状态值
        int timeAndStatusStartX = 0;
        int timeAndStatusStartY = nameStartY + nameRectY + (int) (biHeight * 0.02f);
        int timeAndStatusRectX = biWidth;
        int timeAndStatusRectY = (int) (biHeight * 0.085f);
        BufferedImage subTimeAndStatusImage = ImageHelper.getSubImage(bi, timeAndStatusStartX, timeAndStatusStartY, timeAndStatusRectX, timeAndStatusRectY);
        // 剪裁截图时间和状态值
        int qrcodeStartX = 0;
        int qrcodeStartY = timeAndStatusStartY + timeAndStatusRectY + (int) (biHeight * 0.03f);
        int qrcodeRectX = biWidth;
        int qrcodeRectY = (int) (biHeight * 0.17f);
        BufferedImage subQrcodeImage = ImageHelper.getSubImage(bi, qrcodeStartX, qrcodeStartY, qrcodeRectX, qrcodeRectY);
        BufferedImage binarySubCodeImage = ImageHelper.convertImageToBinary(subQrcodeImage);
        String qrcodeContent = QrcodeUtilz.readQRCode(binarySubCodeImage);
        QrCode qrCode = JSON.parseObject(qrcodeContent, QrCode.class);
        result.setQrcodeContent(qrCode);
        log.info("qrcodeContent :{}", qrcodeContent);
        // 处理文件写入
        boolean writeResult = ImageIO.write(binarySubCodeImage, "jpeg", new File("E://12315.jpeg"));
        log.info("writeResult -----> :{}", writeResult);
        // 识别时间和状态
        int pageIteratorLevel = ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE;
        List<Word> wordList = instance.getWords(subTimeAndStatusImage, pageIteratorLevel);
        int listSize = wordList.size();
        if (listSize > 0) {
            Word timeWord = wordList.get(0);
            String timeText = timeWord.getText();
            String timeInfo = StringUtilz.extractHealthyTimeInfo(timeText);
            result.setTime(timeInfo);
        }
        if (listSize > 1) {
            Word statusWord = wordList.get(1);
            String statusText = statusWord.getText();
            String statusInfo = StringUtilz.extractHealthyStatusInfo(statusText);
            result.setStatus(statusInfo);
        }
        return result;
    }

    @GetMapping("/travelCard")
    public TravelRes travelCard() throws Exception {
        TravelRes result = new TravelRes();
        log.info("ENV : dataPath : {}", dataPath);
        // 创建实例
        ITesseract instance = new Tesseract();
        // 训练文件路径
        instance.setDatapath(dataPath);
        // 设置识别语言
        instance.setLanguage("chi_sim");
        // 设置识别引擎 ITessAPI TessOcrEngineMode.class
        instance.setOcrEngineMode(1);
        InputStream healthyCodeIs = OcrController.class.getClassLoader().getResourceAsStream("sample/travelCard.jpg");
        if (healthyCodeIs == null) {
            throw new RuntimeException("找不到行程码文件");
        }
        // 图像预处理
        BufferedImage bi = ImageIO.read(healthyCodeIs);
//        BufferedImage binaryImage = ImageHelper.convertImageToBinary(bi);
//        // 处理文件写入
//        boolean writeResult = ImageIO.write(binaryImage, "jpeg", new File("E://12345.jpeg"));
//        log.info("writeResult -----> :{}", writeResult);

        int pageIteratorLevel = ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE;
        List<Word> wordList = instance.getWords(bi, pageIteratorLevel);
        int hitColorTipsIndex = -1;
        int hitPhoneIndex = -1;
        int hitTimeIndex = -1;
        boolean startPath = false;
        String longPath = "";
        List<String> eachCityList = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            String text = word.getText();
            String textStr = text.replaceAll(" ", "");
            if (textStr.contains("请收下")) {
                hitColorTipsIndex = i;
                String status = StringUtilz.extractTravelStatus(textStr);
                result.setStatus(status);
            } else if (textStr.contains("动态")) {
                hitPhoneIndex = i;
                String phone = StringUtilz.extractTravelPhone(textStr);
                result.setPhone(phone);
            } else if (textStr.contains("更新于")) {
                // TODO 二值化时间丢失，需要直方图双高峰低谷的点作为阈值而不是127
                hitTimeIndex = i;
                String time = StringUtilz.extractTravelTime(text);// text非textStr
                time = time.replaceAll("\\.", "-");
                result.setTime(time);
            } else if (textStr.contains("途经")) {// 读到市结尾, card2发现连途径两字都识别不出
                int lastColonIndex = textStr.lastIndexOf(":");
                String substring = textStr.substring(lastColonIndex + 1, textStr.length() - 1);
                longPath += substring;
                startPath = true;
            } else if (startPath) {
                longPath += textStr;
            }
        }
        String path = longPath.substring(0, longPath.lastIndexOf("市"));
        String[] pathArray = StringUtils.split(path, "市");
        List<String> pathList = Arrays.asList(pathArray);
        result.setPath(pathList);
//        result.setLongPath(longPath);
//        String content = instance.doOCR(bi);
//        result.setContent(content);
        return result;
    }

    @GetMapping("/paddleOcr")
    public PaddleOcrResult paddleOcr() throws Exception {
        PaddleOcrResult result = new PaddleOcrResult();
        List<String> inputResult = new ArrayList<>();
        List<String> errorResult = new ArrayList<>();
        result.setInputResult(inputResult);
        result.setErrorResult(errorResult);
//        Process exec = Runtime.getRuntime().exec("java -version");
//        Process exec = Runtime.getRuntime().exec("/opt/anaconda3/envs/paddle_env/bin/paddleocr --image_dir /Users/syrianazh/Desktop/ppocr_img/subqrcode.jpg --use_angle_cls true --use_gpu false");
        Process exec = Runtime.getRuntime().exec("/opt/anaconda3/envs/paddle_env/bin/paddleocr --image_dir /Users/syrianazh/Desktop/ppocr_img/qrcode2.jpg --use_angle_cls true --use_gpu false --rec_model_dir /Users/syrianazh/Desktop/ch_ppocr_server_v2.0_rec_infer --det_model_dir /Users/syrianazh/Desktop/ch_ppocr_server_v2.0_det_infer");
//        Process exec = Runtime.getRuntime().exec("/opt/anaconda3/envs/paddle_env/bin/paddleocr --image_dir /Users/syrianazh/Desktop/ppocr_img/subqrcode.jpg --use_gpu false");
//        Process exec = Runtime.getRuntime().exec("/opt/anaconda3/envs/paddle_env/bin/paddleocr --image_dir /Users/syrianazh/Desktop/ppocr_img/subqrcode.jpg --use_angle_cls true --use_gpu false --rec_model_dir /Users/syrianazh/Desktop/ch_ppocr_server_v2.0_rec_infer --det_model_dir /Users/syrianazh/Desktop/ch_ppocr_server_v2.0_det_infer");
        //        String[] cmd = new String[]{"java", "-version"};
//        Process exec = Runtime.getRuntime().exec(cmd);
//        Process exec = Runtime.getRuntime().exec("ps -au");
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        BufferedReader errorStream = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
        String line;
        while ((line = inputStream.readLine()) != null) {
            log.info(line);
            inputResult.add(line);
        }
        while ((line = errorStream.readLine()) != null) {
            log.info(line);
            errorResult.add(line);
        }
        inputStream.close();
        errorStream.close();
        int exitValue = exec.exitValue();
        if (exitValue == 0) {// 成功

        } else {
            throw new RuntimeException("识别图片出错");
        }
        result.setExitValue(exitValue);
        result.setExitValue(null);
        return result;
    }

    @Data
    static class PaddleOcrResult {
        private List<String> inputResult;
        private List<String> errorResult;
        private Integer exitValue;
        private Integer waitForValue;
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
     * <p>
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
    static class OcrRes {
        private String name;
        private String time;
        private String status;
        private QrCode qrcodeContent;
    }

    @Data
    static class QrCode {
        private String codeId;
        private String dueTime;
    }

    @Data
    static class TravelRes {
        private String status;
        private String time;
        private String phone;
        private List<String> path;
//        private String longPath;
//        private String content;
    }
}
