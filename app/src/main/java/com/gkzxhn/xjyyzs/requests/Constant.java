package com.gkzxhn.xjyyzs.requests;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:常量类
 */

public class Constant {

    public static final String URL_HEAD = "http://" +
            //正式url
            "103.37.158.17:3000/" +

            //测试(颂哥电脑)
            // "192.168.1.31:3000/"+

            "api/v1/";

    public static final String BASE_NEW_APK = "http://" +

            //正式url
            "103.37.158.17:8080";

            //测试（颂哥电脑）
            //"192.168.1.112:8080";


    public static final String NEW_APK = BASE_NEW_APK +

            "/xjp/file/downloadFile";

    public static final String CUSTOM_SIMILARITY="custom_similarity";//人脸识别对比系数
    public static final boolean IS_REMOTE_INTERVIEW=true;//是否调用远程会见
}
