package com.sh.ocr.res.common;

import lombok.Data;

/**
 * @author syriana.zh
 * @date 2020/06/23
 */
@Data
public class RestResult<T> {
    private String msg;
    private Boolean succeed = true;
    private T data;
    private String code = "200";
    private String requestId;


    public static <T> RestResult suc(String message, T data){
        RestResult restResult = new RestResult();
        restResult.setData(data);
        return restResult;
    }

    public static <T> RestResult suc(String message){
        RestResult restResult = new RestResult();
        restResult.setMsg(message);
        return restResult;
    }
}
