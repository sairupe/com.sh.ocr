package com.sh.ocr.utils;

import com.sh.ocr.constant.OcrConstatants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilz {

    public static String extractHealthyNameInfo(String nameStr) throws Exception {
        if (isEmpty(nameStr)) {
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

    public static String extractHealthyTimeInfo(String timeStr) throws Exception {
        if (isEmpty(timeStr)) {
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

    public static boolean isEmpty(String str) {
        return str == null || OcrConstatants.EMPTY_STRING.equals(str);
    }

    public static String extractHealthyStatusInfo(String statusStr) throws Exception {
        if (isEmpty(statusStr)) {
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

    public static String extractTravelStatus(String statusStr) throws Exception {
        if (isEmpty(statusStr)) {
            return OcrConstatants.EMPTY_STRING;
        }
        statusStr = statusStr.replaceAll(" ", "");
        String statusRegex = "[\\u4E00-\\u9FA5]色";
        Pattern statusP = Pattern.compile(statusRegex);
        Matcher statusM = statusP.matcher(statusStr);
        while (statusM.find()) {
            String statusInfo = statusM.group();
            return statusInfo;
        }
        return OcrConstatants.EMPTY_STRING;
    }


    public static String extractTravelPhone(String phoneStr) throws Exception {
        if (isEmpty(phoneStr)) {
            return OcrConstatants.EMPTY_STRING;
        }
        phoneStr = phoneStr.replaceAll(" ", "");
        String phoneRegex = "\\d+";
        Pattern statusP = Pattern.compile(phoneRegex);
        Matcher statusM = statusP.matcher(phoneStr);
        List<String> findList = new ArrayList<>();
        while (statusM.find()) {
            String statusInfo = statusM.group();
            findList.add(statusInfo);
        }
        String first = "";
        String last = "";
        if (findList.size() > 0) {
            first = findList.get(0);
        }
        if (findList.size() > 1) {
            last = findList.get(1);
        }
        return first + "****" + last;
    }

    public static String extractTravelTime(String timeStr) throws Exception {
        if (isEmpty(timeStr)) {
            return OcrConstatants.EMPTY_STRING;
        }
        String timeRegex = "\\d{4}.\\d{1,2}.\\d{1,2} ([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";
        Pattern timeP = Pattern.compile(timeRegex);
        Matcher timeM = timeP.matcher(timeStr);
        while (timeM.find()) {
            String time = timeM.group();
            return time;
        }
        return OcrConstatants.EMPTY_STRING;
    }

}
