package com.gkzxhn.xjyyzs.entities.events;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/10/13.
 * description:清除消息结果通知
 */

public class ClearMsgResult {

    private boolean isSuccess;

    public ClearMsgResult(){}

    public ClearMsgResult(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
