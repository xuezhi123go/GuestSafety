package com.gkzxhn.xjyyzs.requests.bean;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/9.
 * description:更新信息  版本更新检查返回json
 */

public class UpdateInfo {

    /**
     * versionCode : 2
     * versionName : 1.0
     * content : 修改了一些bug
     */

    private int versionCode;
    private String versionName;
    private String content;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
