package com.gkzxhn.xjyyzs.entities;

/**
 * Author: Huang ZN
 * Date: 2016/12/14
 * Email:943852572@qq.com
 * Description:申请会见结果
 */

public class BookResult {


    /**
     * code : 401
     * msg : 该日期不能申请会见
     */

    private int code;
    private String msg;
    private String relationship;
//    private String  prisonerId;
//
//    public String getPrisonerId() {
//        return prisonerId;
//    }
//
//
//    public void setPrisonerId(String prisonerId) {
//        this.prisonerId = prisonerId;
//    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BookResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
