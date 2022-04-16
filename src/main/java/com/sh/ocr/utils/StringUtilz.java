package com.sh.ocr.utils;

import com.sh.ocr.constant.OcrConstatants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilz {

    public static String extractNameInfo(String nameStr) throws Exception {
        if(isEmpty(nameStr)){
            return OcrConstatants.EMPTY_STRING;
        }
        nameStr = nameStr.replaceAll(" ", "");
        String nameRegex = "[\\u4E00-\\u9FA5]+";
        Pattern nameP = Pattern.compile(nameRegex);
        Matcher nameM = nameP.matcher(nameStr);
        while (nameM.find()) {
            String nameInfo = nameM.group();
            return nameInfo + "*";
        }
        return OcrConstatants.EMPTY_STRING;
    }

    public static String extractTimeInfo(String timeStr) throws Exception {
        if(isEmpty(timeStr)){
            return OcrConstatants.EMPTY_STRING;
        }
        String timeRegex = "\\d{4}(\\-)\\d{1,2}(\\-)\\d{1,2} ([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
        Pattern timeP = Pattern.compile(timeRegex);
        Matcher timeM = timeP.matcher(timeStr);
        while (timeM.find()) {
            String timeInfo = timeM.group();
            return timeInfo;
        }
        return OcrConstatants.EMPTY_STRING;
    }

    public static boolean isEmpty(String str){
        return str == null || OcrConstatants.EMPTY_STRING.equals(str);
    }

    public static String extractStatusInfo(String statusStr) throws Exception {
        if(isEmpty(statusStr)){
            return OcrConstatants.EMPTY_STRING;
        }
        statusStr = statusStr.replaceAll(" ", "");
        String statusRegex = "绿码|黄码|红码";
        Pattern statusP = Pattern.compile(statusRegex);
        Matcher statusM = statusP.matcher(statusStr);
        while (statusM.find()) {
            String statusInfo = statusM.group();
            return statusInfo;
        }
        return OcrConstatants.EMPTY_STRING;
    }

}
