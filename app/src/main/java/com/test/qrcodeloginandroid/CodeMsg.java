package com.test.qrcodeloginandroid;

/**
 * Created by wangyongxin on 2018/1/30.
 */

public class CodeMsg<S, E> {
    public String type;
    public int code;
    public S content;
    public E errMsg;

    public boolean isSuccess(){
        return "success".equals(type);
    }
}
