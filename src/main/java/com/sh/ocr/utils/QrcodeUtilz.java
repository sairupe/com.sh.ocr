package com.sh.ocr.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Slf4j
public class QrcodeUtilz {

    public static String readQRCode(BufferedImage qrcodeBi) {
        MultiFormatReader formatReader = new MultiFormatReader();
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(qrcodeBi)));

        // 定义二维码的参数
        HashMap hints = new HashMap();
        // 编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            Result result = formatReader.decode(binaryBitmap, hints);
            String text = result.getText();
            return text;
        } catch (NotFoundException e){
            log.error(" ---> 未能识别出二维码信息");
        }
        return null;
    }
}
